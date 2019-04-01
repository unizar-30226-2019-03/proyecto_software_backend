package com.unicast.unicast_backend;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;

import com.unicast.unicast_backend.persistance.model.Comment;
import com.unicast.unicast_backend.persistance.model.Message;
import com.unicast.unicast_backend.persistance.model.Notification;
import com.unicast.unicast_backend.persistance.model.NotificationCategory;
import com.unicast.unicast_backend.persistance.model.Privilege;
import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.model.Role;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.University;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.model.VideoTag;
import com.unicast.unicast_backend.persistance.repository.CommentRepository;
import com.unicast.unicast_backend.persistance.repository.MessageRepository;
import com.unicast.unicast_backend.persistance.repository.NotificationCategoryRepository;
import com.unicast.unicast_backend.persistance.repository.NotificationRepository;
import com.unicast.unicast_backend.persistance.repository.PrivilegeRepository;
import com.unicast.unicast_backend.persistance.repository.ReproductionListRepository;
import com.unicast.unicast_backend.persistance.repository.RoleRepository;
import com.unicast.unicast_backend.persistance.repository.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.UniversityRepository;
import com.unicast.unicast_backend.persistance.repository.UserRepository;
import com.unicast.unicast_backend.persistance.repository.VideoRepository;
import com.unicast.unicast_backend.persistance.repository.VideoTagRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnicastBackendApplicationTests {

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
	private UniversityRepository universityRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VideoRepository videoRepository;

	@Autowired
	private VideoTagRepository videoTagRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

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

	private User createTestUser() throws URISyntaxException {
		User user = new User();

		user.setUsername("UsernameTest");
		user.setEmail("test@test.com");
		user.setPhoto(new URI("http://s3.amazonaws.com/bucket"));
		user.setDescription("Description Test");
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode("test"));

		return user;
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

		return subject;
	}

	private Video createTestVideo() throws URISyntaxException {
		Video video = new Video();

		video.setTitle("Video test");
		video.setDescription("Description test");
		video.setTimestamp(new Timestamp(1000));
		video.setS3Path(new URI("http://s3.amazonaws.com/bucket"));

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
