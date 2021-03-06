import javafx.geometry.Pos;
import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

import javax.jws.soap.SOAPBinding;

public class BasicTest extends UnitTest {

    //method to delete the database before each test
    @Before
    public void setup()
    {
        Fixtures.deleteDatabase();
    }

    @Test
    public void createAndRetrieveUser() {

        //Create a new user and save it
        new User("bob@gmail.com","secret" ,"Bob" ).save();

        //Retrieve the user with e-mail address bob@gmail.com
        User bob = User.find("byEmail", "bob@gmail.com").first();

        //Test
        assertNotNull(bob);
        assertEquals("Bob", bob.fullname);
    }

    @Test
    public void tryConnectAsUser()
    {

        //Create a new user and save it
        new User("bob@gmail.com", "secret", "Bob").save();

        //Test
        assertNotNull(User.connect("bob@gmail.com", "secret"));
        assertNull(User.connect("bob@gmail.com", "badsecret"));
        assertNull(User.connect("tom@gmail.com", "secret"));
    }

    @Test
    public void createPost()
    {

        //Create a new user and save it
        User bob = new User("bob@gmail.com", "secret", "Bob").save();

        //Create a new post
        new Post(bob, "My first post", "Hello world").save();

        //Test that the post has been created
        assertEquals(1, Post.count());

        //Retrieve all posts created by Bob
        List<Post> bobPosts = Post.find("byAuthor", bob).fetch();

        //Tests
        assertEquals(1, bobPosts.size());

        Post firstPost = bobPosts.get(0);
        assertNotNull(firstPost);
        assertEquals(bob, firstPost.author);
        assertEquals("My first post", firstPost.title);
        assertEquals("Hello world", firstPost.content);
        assertNotNull(firstPost.postedAt);

    }

    @Test
    public void postComments()
    {
        //Create a new user and save it
        User bob = new User("bob@gmail.com", "secret", "Bob").save();

        //Create a new post
        Post bobPost = new Post(bob, "My first post", "Hello world").save();

        //Post a first comment
        new Comment(bobPost, "Jeff", "Nice post").save();
        new Comment(bobPost, "Tom", "I knew that !").save();

        //Retrieve all comments
        List<Comment> bobPostComments = Comment.find("byPost", bobPost).fetch();

        //Tests
        assertEquals(2, bobPostComments.size());

        Comment firstComment = bobPostComments.get(0);
        assertNotNull(firstComment);
        assertEquals("Jeff", firstComment.author);
        assertEquals("Nice post", firstComment.content);
        assertNotNull(firstComment.postedAt);

        Comment secondComment = bobPostComments.get(1);
        assertNotNull(secondComment);
        assertEquals("Tom",secondComment.author);
        assertEquals("I knew that !", secondComment.content);
        assertNotNull(secondComment.postedAt);
    }
}
