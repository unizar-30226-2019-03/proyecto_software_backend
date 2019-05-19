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

@Component
public class RecommenderSystem {
    @Autowired
    private UserRepository userRepository;

    @Autowired 
	private VideoRepository videoRepository;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    public Pair<File, List<Long>> generateDataSet() throws IOException {
        File tempFile = File.createTempFile("recommender", "system.txt");

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

    // TODO: cambiar frecuencia de cron, ahora esta cada minuto
    @Transactional
    @Scheduled(cron = "0 * * * * *")
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
