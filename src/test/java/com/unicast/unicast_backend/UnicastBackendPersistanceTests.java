package com.unicast.unicast_backend;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.unicast.unicast_backend.persistance.model.Comment;
import com.unicast.unicast_backend.persistance.model.Contains;
import com.unicast.unicast_backend.persistance.model.ContainsKey;
import com.unicast.unicast_backend.persistance.model.Degree;
import com.unicast.unicast_backend.persistance.model.Display;
import com.unicast.unicast_backend.persistance.model.DisplayKey;
import com.unicast.unicast_backend.persistance.model.Message;
import com.unicast.unicast_backend.persistance.model.Notification;
import com.unicast.unicast_backend.persistance.model.NotificationCategory;
import com.unicast.unicast_backend.persistance.model.Privilege;
import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.model.Role;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.University;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.model.UserIsNotifiedKey;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.model.VideoTag;
import com.unicast.unicast_backend.persistance.model.Vote;
import com.unicast.unicast_backend.persistance.model.VoteKey;
import com.unicast.unicast_backend.persistance.repository.CommentRepository;
import com.unicast.unicast_backend.persistance.repository.ContainsRepository;
import com.unicast.unicast_backend.persistance.repository.DegreeRepository;
import com.unicast.unicast_backend.persistance.repository.DisplayRepository;
import com.unicast.unicast_backend.persistance.repository.MessageRepository;
import com.unicast.unicast_backend.persistance.repository.NotificationCategoryRepository;
import com.unicast.unicast_backend.persistance.repository.NotificationRepository;
import com.unicast.unicast_backend.persistance.repository.PrivilegeRepository;
import com.unicast.unicast_backend.persistance.repository.ReproductionListRepository;
import com.unicast.unicast_backend.persistance.repository.RoleRepository;
import com.unicast.unicast_backend.persistance.repository.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.UniversityRepository;
import com.unicast.unicast_backend.persistance.repository.UserIsNotifiedRepository;
import com.unicast.unicast_backend.persistance.repository.UserRepository;
import com.unicast.unicast_backend.persistance.repository.VideoRepository;
import com.unicast.unicast_backend.persistance.repository.VideoTagRepository;
import com.unicast.unicast_backend.persistance.repository.VoteRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnicastBackendPersistanceTests {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private NotificationCategoryRepository notificationCategoryRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private ReproductionListRepository reproductionListRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private DegreeRepository degreeRepository;

	@Autowired
	private UniversityRepository universityRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VideoRepository videoRepository;

	@Autowired
	private VideoTagRepository videoTagRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private VoteRepository voteRepository;
	
	@Autowired
	private UserIsNotifiedRepository userIsNotifiedRepository;

	@Autowired
	private DisplayRepository displayRepository;

	@Autowired
	private ContainsRepository containsRepository;


	@Test
	public void contextLoads() {
	}

	@Test
	@Transactional
	public void testNotificationCategory() {
		NotificationCategory notificationCategory = createTestNotificationCategory();
		notificationCategoryRepository.save(notificationCategory);

		final String testName = "#144881: TEST NAME";
		notificationCategory.setName(testName);
		notificationCategoryRepository.save(notificationCategory);

		NotificationCategory notificationCategoryBD = notificationCategoryRepository.findByName(testName);

		assertEquals(notificationCategory, notificationCategoryBD);

		notificationCategoryRepository.delete(notificationCategory);
	}

	@Test
	@Transactional
	public void testNotification() {
		NotificationCategory notificationCategory = createTestNotificationCategory();
		notificationCategoryRepository.save(notificationCategory);
		
		Notification notification = createTestNotification();
		notification.setCategory(notificationCategory);
		notificationRepository.save(notification);

		Notification notificationBD = notificationRepository.findById(notification.getId()).get();

		assertEquals(notification, notificationBD);

		notificationRepository.delete(notification);
		notificationCategoryRepository.delete(notificationCategory);
	}

	@Test
	@Transactional
	public void testUniversity() {
		University university = createTestUniversity();
		universityRepository.save(university);

		final String testName = "#144881: TEST NAME";
		university.setName(testName);
		universityRepository.save(university);

		University universityBD = universityRepository.findByName(testName);

		assertEquals(university, universityBD);

		universityRepository.delete(university);
	}

	@Test
	@Transactional
	public void testUser() throws URISyntaxException {
		University university = createTestUniversity();
		universityRepository.save(university);

		List<Subject> subjects = new ArrayList<>();
		subjects.add(createTestSubject());
		Subject testSubject = createTestSubject();
		testSubject.setName("NOMBRE TEST 2 SUBJECT");
		testSubject.setAbbreviation("ASDDS");
		subjects.add(testSubject);
		subjectRepository.saveAll(subjects);

		User user = createTestUser();
		userRepository.save(user);

		final String testName = "#144881: TEST NAME";
		user.setUsername(testName);
		userRepository.save(user);

		User UserBD = userRepository.findByUsername(testName);

		assertEquals(user, UserBD);

		universityRepository.delete(university);
		subjectRepository.deleteInBatch(subjects);
		userRepository.delete(user);
	}

	@Test
	@Transactional
	public void testSubject() {
		University university = createTestUniversity();
		universityRepository.save(university);
		
		Subject subject = createTestSubject();
		subject.setUniversity(university);
		subjectRepository.save(subject);

		Subject subjectBD = subjectRepository.findById(subject.getId()).get();

		assertEquals(subject, subjectBD);

		subjectRepository.delete(subject);
		universityRepository.delete(university);
	}

	@Test
	@Transactional
	public void testVideoTag() {
		VideoTag videoTag = createTestVideoTag();
		videoTagRepository.save(videoTag);

		final String testName = "#144881: TEST NAME";
		videoTag.setName(testName);
		videoTagRepository.save(videoTag);

		VideoTag videoTagBD = videoTagRepository.findByName(testName);

		assertEquals(videoTag, videoTagBD);

		videoTagRepository.delete(videoTag);
	}

	@Test
	@Transactional
	public void testReproductionList() throws URISyntaxException {
		User user = createTestUser();
		userRepository.save(user);

		ReproductionList reproductionList = createTestReproductionList();
		reproductionList.setUser(user);
		reproductionListRepository.save(reproductionList);

		final String testName = "#144881: TEST NAME";
		reproductionList.setName(testName);
		reproductionListRepository.save(reproductionList);

		ReproductionList reproductionListBD = reproductionListRepository.findByName(testName).get(0);

		assertEquals(reproductionList, reproductionListBD);

		userRepository.delete(user);
		reproductionListRepository.delete(reproductionList);
	}

	@Test
	@Transactional
	public void testMessage() throws URISyntaxException {
		User sender = createTestUser();
		userRepository.save(sender);

		User receiver = createTestUser();
		receiver.setUsername("UsernameTestReceiver");
		receiver.setPhoto(new URI("http://s3.amazonaws.com/bucket/photo/2"));
		receiver.setEmail("asd@asd.com");
		userRepository.save(receiver);

		Message message = createTestMessage();
		message.setSender(sender);
		message.setReceiver(receiver);
		messageRepository.save(message);

		final String testName = "#144881: TEST NAME";
		message.setText(testName);
		messageRepository.save(message);

		Message messageBD = messageRepository.findById(message.getId()).get();

		assertEquals(message, messageBD);

		userRepository.delete(sender);
		userRepository.delete(receiver);
		messageRepository.delete(message);
	}

	@Test
	@Transactional
	public void testComment() throws URISyntaxException {
		Subject subject = createTestSubject();
		subjectRepository.save(subject);

		User user = createTestUser();
		userRepository.save(user);

		Video video = createTestVideo();
		video.setSubject(subject);
		video.setUploader(user);
		videoRepository.save(video);

		Comment comment = createTestComment();
		comment.setVideo(video);
		comment.setUser(user);
		commentRepository.save(comment);

		final String testName = "#144881: TEST NAME";
		comment.setText(testName);
		commentRepository.save(comment);

		Comment commentBD = commentRepository.findById(comment.getId()).get();

		assertEquals(comment, commentBD);

		subjectRepository.delete(subject);
		userRepository.delete(user);
		videoRepository.delete(video);
		commentRepository.delete(comment);
	}

	@Test
	@Transactional
	public void testVideo() throws URISyntaxException {
		User uploader = createTestUser();
		userRepository.save(uploader);

		Subject subject = createTestSubject();
		subjectRepository.save(subject);

		List<VideoTag> videoTags = new ArrayList<>();
		videoTags.add(createTestVideoTag());
		VideoTag videoTagTest = createTestVideoTag();
		videoTagTest.setName("NOMBRE VIDEOTAG TEST 2");
		videoTags.add(videoTagTest);
		videoTagRepository.saveAll(videoTags);
		
		Video video = createTestVideo();
		video.setSubject(subject);
		video.setUploader(uploader);
		video.setTags(videoTags);
		videoRepository.save(video);

		Video videoBD = videoRepository.findById(video.getId()).get();

		assertEquals(video, videoBD);

		userRepository.delete(uploader);
		videoTagRepository.deleteInBatch(videoTags);
		videoRepository.delete(video);
		subjectRepository.delete(subject);
	}

	@Test
	@Transactional
	public void testDegree() throws URISyntaxException {
		Degree degree = createTestDegree();

		List<User> users = new ArrayList<>();
		users.add(createTestUser());
		userRepository.saveAll(users);
		List<University> universities = new ArrayList<>();
		universities.add(createTestUniversity());
		universityRepository.saveAll(universities);
		List<Subject> subjects = new ArrayList<>();
		subjects.add(createTestSubject());
		subjectRepository.saveAll(subjects);
		degree.setSubjects(subjects);
		degree.setUniversities(universities);
		degree.setUsers(users);

		degreeRepository.save(degree);
		

		Degree degreeBD = degreeRepository.findById(degree.getId()).get();

		assertEquals(degree, degreeBD);

		userRepository.deleteInBatch(users);
		subjectRepository.deleteInBatch(subjects);
		universityRepository.deleteInBatch(universities);
		degreeRepository.delete(degree);

	}

	@Test
	@Transactional
	public void testVote() throws URISyntaxException {
		Vote vote = createTestVote();

		User user = createTestUser();
		userRepository.save(user);

		Subject subject = createTestSubject();
		subjectRepository.save(subject);

		List<VideoTag> videoTags = new ArrayList<>();
		videoTags.add(createTestVideoTag());
		VideoTag videoTagTest = createTestVideoTag();
		videoTagTest.setName("NOMBRE VIDEOTAG TEST 2");
		videoTags.add(videoTagTest);
		videoTagRepository.saveAll(videoTags);
		
		Video video = createTestVideo();
		video.setSubject(subject);
		video.setTags(videoTags);
		video.setUploader(user);
		videoRepository.save(video);

		videoRepository.save(video);
		
		VoteKey voteId = new VoteKey();

		voteId.setUserId(user.getId());
		voteId.setVideoId(video.getId());

		vote.setId(voteId);
		vote.setUser(user);
		vote.setVideo(video);

		voteRepository.save(vote);

		Vote voteDB = voteRepository.findById(vote.getId()).get();

		assertEquals(vote, voteDB);

		subjectRepository.delete(subject);
		userRepository.delete(user);
		videoRepository.delete(video);
		voteRepository.delete(vote);
		videoTagRepository.deleteInBatch(videoTags);
 	}

	private Vote createTestVote() {
		Vote v = new Vote();
		
		v.setClarity(8);
		v.setDislikes(34);
		v.setLikes(45);
		v.setQuality(23);
		return v;
	}

	private UserIsNotified createTestUserIsNotified() {
		UserIsNotified u = new UserIsNotified();	
		u.setChecked(true);

		return u;
	}

	@Test
	@Transactional
	public void testUserIsNotified() throws URISyntaxException {
		NotificationCategory notificationCategory = createTestNotificationCategory();
		notificationCategoryRepository.save(notificationCategory);
		UserIsNotified userIsNotified = createTestUserIsNotified();
		User user = createTestUser();
		Notification notification = createTestNotification();
		notification.setCategory(notificationCategory);
		userRepository.save(user);
		notificationRepository.save(notification);
		
		userIsNotified.setUser(user);
		userIsNotified.setNotification(notification);

		UserIsNotifiedKey key = new UserIsNotifiedKey();
		key.setNotificationId(notification.getId());
		key.setUserId(user.getId());

		userIsNotified.setId(key);

		userIsNotifiedRepository.save(userIsNotified);

		UserIsNotified userIsNotifiedBD = userIsNotifiedRepository.findById(userIsNotified.getId()).get();

		assertEquals(userIsNotified, userIsNotifiedBD);

		userIsNotifiedRepository.delete(userIsNotified);
		userRepository.delete(user);
		notificationRepository.delete(notification);
		notificationCategoryRepository.delete(notificationCategory);
	}

	private Display createDisplayTest() {
		Display d = new Display();
		Timestamp t = Timestamp.from(Instant.now());
		d.setSecsFromBeg(200);
		d.setTimestampLastTime(t);
		return d;
	}


	@Test
	@Transactional
	public void testDisplay() throws URISyntaxException {
		Display display = createDisplayTest();

		User user = createTestUser();
		userRepository.save(user);

		Subject subject = createTestSubject();
		subjectRepository.save(subject);

		List<VideoTag> videoTags = new ArrayList<>();
		videoTags.add(createTestVideoTag());
		VideoTag videoTagTest = createTestVideoTag();
		videoTagTest.setName("NOMBRE VIDEOTAG TEST 2");
		videoTags.add(videoTagTest);
		videoTagRepository.saveAll(videoTags);
		
		Video video = createTestVideo();
		video.setSubject(subject);
		video.setUploader(user);
		video.setTags(videoTags);
		videoRepository.save(video);
		
		display.setUser(user);
		display.setVideo(video);

		DisplayKey key = new DisplayKey();
		key.setUserId(user.getId());
		key.setVideoId(video.getId());

		display.setId(key);

		displayRepository.save(display);

		Display displayBD = displayRepository.findById(display.getId()).get();

		assertEquals(display, displayBD);


		subjectRepository.delete(subject);
		userRepository.delete(user);
		videoRepository.delete(video);
		displayRepository.delete(display);
		videoTagRepository.deleteInBatch(videoTags);
	}


	private Contains createTestContains(){
		Contains c = new Contains();
		c.setPosition(23);

		return c;
	}

	@Test
	@Transactional
	public void testContains() throws URISyntaxException {
		Contains contains = createTestContains();

		Subject subject = createTestSubject();
		subjectRepository.save(subject);

		List<VideoTag> videoTags = new ArrayList<>();
		videoTags.add(createTestVideoTag());
		VideoTag videoTagTest = createTestVideoTag();
		videoTagTest.setName("NOMBRE VIDEOTAG TEST 2");
		videoTags.add(videoTagTest);
		videoTagRepository.saveAll(videoTags);

		User user = createTestUser();
		userRepository.save(user);

		Video video = createTestVideo();
		video.setSubject(subject);
		video.setTags(videoTags);
		video.setUploader(user);
		videoRepository.save(video);

		ReproductionList reproductionList = createTestReproductionList();
		reproductionList.setUser(user);
		reproductionListRepository.save(reproductionList);

		final String testName = "#144881: TEST NAME";
		reproductionList.setName(testName);
		reproductionListRepository.save(reproductionList);

		contains.setVideo(video);
		contains.setList(reproductionList);

		ContainsKey key = new ContainsKey();
		key.setListId(reproductionList.getId());
		key.setVideoId(video.getId());

		contains.setId(key);

		containsRepository.save(contains);

		

		Contains containsBD = containsRepository.findById(contains.getId()).get();

		assertEquals(contains,containsBD);

		subjectRepository.delete(subject);
		userRepository.delete(user);
		videoRepository.delete(video);
		containsRepository.delete(contains);
		videoTagRepository.deleteInBatch(videoTags);
		reproductionListRepository.delete(reproductionList);

	}

	private User createTestUser() throws URISyntaxException {
		User user = new User();

		user.setUsername("UsernameTest32432dscdf");
		user.setName("UsernameTestasdasdsad");
		user.setSurnames("Sasdasduahe4nafdsono3nofn10nf");
		user.setEmail("test@test.com");
		user.setPhoto(new URI("http://s3.amazonaws.com/bucket/photo"));
		user.setDescription("Description Test");
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode("test"));

		return user;
	}


	private Degree createTestDegree() throws URISyntaxException {
		Degree degree = new Degree();

		degree.setName("DegreeTest");

		return degree;
	}


	private VideoTag createTestVideoTag() {
		VideoTag videoTag = new VideoTag();

		videoTag.setName("Name test");

		return videoTag;
	}

	private University createTestUniversity() {
		University university = new University();

		university.setName("Name test");

		return university;
	}

	private Role createTestRole() {
		Role role = new Role();

		role.setName("Name test");

		return role;
	}

	private ReproductionList createTestReproductionList() {
		ReproductionList reproductionList = new ReproductionList();

		reproductionList.setName("Name test");

		return reproductionList;
	}

	private Privilege createTestPrivilege() {
		Privilege privilege = new Privilege();

		privilege.setName("Name test");

		return privilege;
	}

	private Notification createTestNotification() {
		Notification notification = new Notification();

		notification.setText("Text test");
		notification.setTimestamp(new Timestamp(1000));

		return notification;
	}

	private NotificationCategory createTestNotificationCategory() {
		NotificationCategory notificationCategory = new NotificationCategory();

		notificationCategory.setName("Name test");

		return notificationCategory;
	}

	private Message createTestMessage() {
		Message message = new Message();

		message.setText("Message text test");
		message.setTimestamp(new Timestamp(1000));

		return message;
	}

	private Subject createTestSubject() {
		Subject subject = new Subject();

		subject.setName("Subject test");
		subject.setAbbreviation("ABAB");

		return subject;
	}

	private Video createTestVideo() throws URISyntaxException {
		Video video = new Video();

		video.setTitle("Video test");
		video.setDescription("Description test");
		video.setTimestamp(new Timestamp(1000));
		video.setUrl(new URI("http://s3.amazonaws.com/bucket/video"));
		video.setThumbnailUrl(new URI("http://s3.amazonaws.com/bucket/thumbnail"));

		return video;
	}

	private Comment createTestComment() {
		Comment comment = new Comment();

		comment.setText("Comment text test");
		comment.setTimestamp(new Timestamp(1000));
		comment.setSecondsFromBeginning(100);

		return comment;
	}
}
