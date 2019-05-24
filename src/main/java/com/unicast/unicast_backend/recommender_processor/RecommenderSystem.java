/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.recommender_processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.unicast.unicast_backend.persistance.model.Recommendation;
import com.unicast.unicast_backend.persistance.model.RecommendationKey;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.RecommendationRepository;
import com.unicast.unicast_backend.persistance.repository.rest.DisplayRepository;
import com.unicast.unicast_backend.persistance.repository.rest.UserRepository;
import com.unicast.unicast_backend.persistance.repository.rest.VideoRepository;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.conf.Configuration.Resource;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.ranking.RankSGDRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.PCCSimilarity;
import net.librec.similarity.RecommenderSimilarity;

/*
 * Sistema de recomendaciones para proporcionar videos
 * de interes para los usuarios
 */

@Component
public class RecommenderSystem {

    // Repositorio de usuarios
    @Autowired
    private UserRepository userRepository;

    // Repositorio para los videos
    @Autowired 
	private VideoRepository videoRepository;

    // Repositorio para los displays
    @Autowired
    private DisplayRepository displayRepository;

    // Repositorio para el sistema de recomendaciones
    @Autowired
    private RecommendationRepository recommendationRepository;

    /*
     * Permite procesar las visualizaciones de cada video almacenadas en la base
     * de datos extrayendo los datos de un fichero para poder llevar a cabo las recomendaciones a los usuarios
     */
    public Pair<File, List<Long>> generateDataSet() throws IOException {

        // Creacion del fichero de procesamiento de los datos
        File tempFile = File.createTempFile("recommender", "system.txt");

        // Procesamieto de las visualizaciones de los videos segun la informacion de la bas de datos
        // para posteriormente volcarla en el fichero
        List<Long> userIds = new ArrayList<>();

        Files.write(tempFile.toPath(), () -> displayRepository.findAll().stream().<CharSequence>map(d -> {
            Video vid = d.getVideo();
            Double result = d.getSecsFromBeg() / Double.valueOf(vid.getSeconds());
            if (result > 1) {
                result = 1.0;
            } else if (result < 0) {
                result = 0.0;
            }

            result *= 5;
            userIds.add(d.getId().getUserId());
            return d.getId().getUserId().toString() + " " + d.getId().getVideoId().toString() + " " + result.toString();

        }).iterator());

        return Pair.of(tempFile, userIds);
    }

    /*
     * Permite ejecutar el sistema de recomendaciones de forma automatica cada 10 minutos
     * para que procese los datos del fichero y realice las recomendaciones de videos a los 
     * usuarios
     */
    @Transactional
    @Scheduled(cron = "0 0/10 * * * *")
    public void run() throws LibrecException, IOException {

        Pair<File, List<Long>> fileAndUserIds = generateDataSet();
        File datasetFile = fileAndUserIds.getKey();
        Resource resource = new Resource("librec.properties");
        Configuration conf = new Configuration();
        conf.addResource(resource);
        conf.set("dfs.data.dir", datasetFile.getParent());
        conf.set("data.input.path", datasetFile.getName());

        DataModel dataModel = new TextDataModel(conf);
        dataModel.buildDataModel();

        RecommenderContext context = new RecommenderContext(conf, dataModel);

        RecommenderSimilarity similarity = new PCCSimilarity();
        similarity.buildSimilarityMatrix(dataModel);
        context.setSimilarity(similarity);

        Recommender rankSGDrecommender = new RankSGDRecommender();
        rankSGDrecommender.recommend(context);

        List<RecommendedItem> recommendedItemList = rankSGDrecommender.getRecommendedList();
        
        recommendationRepository.deleteAll();
        List<Recommendation> recommendations = new ArrayList<>();

        Integer position = 1;
        for (RecommendedItem item : recommendedItemList) {
            RecommendationKey rKey = new RecommendationKey();
            rKey.setUserId(Long.valueOf(item.getUserId()));
            rKey.setVideoId(Long.valueOf(item.getItemId()));
            Recommendation recommendation = new Recommendation();
            recommendation.setId(rKey);
            recommendation.setPosition(position);
            recommendations.add(recommendation);
            position++;
        }

        List<Video> topVideos = videoRepository.findOrderBySizeDisplays();
        List<Video> top10Videos = topVideos.subList(0, Math.min(topVideos.size(), 10));

        for (User u : userRepository.findByIdNotIn(fileAndUserIds.getValue())) {
            for (Video v : top10Videos) {
                RecommendationKey rKey = new RecommendationKey();
                rKey.setUserId(Long.valueOf(u.getId()));
                rKey.setVideoId(Long.valueOf(v.getId()));
                Recommendation recommendation = new Recommendation();
                recommendation.setId(rKey);
                recommendation.setPosition(position);
                recommendations.add(recommendation);
                position++;
            }
        }

        recommendationRepository.saveAll(recommendations);

        datasetFile.delete();
    }
}
