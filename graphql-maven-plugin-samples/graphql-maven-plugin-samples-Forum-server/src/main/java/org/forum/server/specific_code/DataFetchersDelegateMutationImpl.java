/**
 * 
 */
package org.forum.server.specific_code;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.forum.server.graphql.Board;
import org.forum.server.graphql.DataFetchersDelegateMutation;
import org.forum.server.graphql.Member;
import org.forum.server.graphql.MemberInput;
import org.forum.server.graphql.Post;
import org.forum.server.graphql.PostInput;
import org.forum.server.graphql.Topic;
import org.forum.server.graphql.TopicInput;
import org.forum.server.jpa.BoardRepository;
import org.forum.server.jpa.PostRepository;
import org.forum.server.jpa.TopicRepository;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetchingEnvironment;
import io.reactivex.subjects.Subject;

/**
 * @author etienne-sf
 */
@Component
public class DataFetchersDelegateMutationImpl implements DataFetchersDelegateMutation {

	@Resource
	BoardRepository boardRepository;
	@Resource
	TopicRepository topicRepository;
	@Resource
	PostRepository postRepository;

	/**
	 * This {@link Subject} will be notified for each Post creation. This is the basis for the <I>subscribeToNewPost</I>
	 * subscription
	 */
	@Resource
	PostPublisher postPublisher;

	Random rand = new Random();

	@Override
	public Board createBoard(DataFetchingEnvironment dataFetchingEnvironment, String name, Boolean publiclyAvailable) {
		Board board = new Board();
		board.setName(name);
		if (publiclyAvailable != null) {
			board.setPubliclyAvailable(publiclyAvailable);
		}
		boardRepository.save(board);
		return board;
	}

	@Override
	public Topic createTopic(DataFetchingEnvironment dataFetchingEnvironment, TopicInput topicInput) {
		Topic newTopic = new Topic();
		newTopic.setBoardId(topicInput.getBoardId());
		newTopic.setAuthorId(topicInput.getInput().getAuthorId());
		newTopic.setPubliclyAvailable(topicInput.getInput().getPubliclyAvailable());
		newTopic.setDate(topicInput.getInput().getDate());
		newTopic.setTitle(topicInput.getInput().getTitle());
		newTopic.setContent(topicInput.getInput().getContent());
		topicRepository.save(newTopic);
		return newTopic;
	}

	@Override
	public Post createPost(DataFetchingEnvironment dataFetchingEnvironment, PostInput postParam) {
		Post newPost = new Post();
		newPost.setId(rand.nextLong()); // A proper use of ids (with a sequence) would be better for production
		newPost.setTopicId(postParam.getTopicId());
		newPost.setDate(postParam.getFrom());
		if (postParam.getInput() != null) {
			newPost.setAuthorId(postParam.getInput().getAuthorId());
			newPost.setPubliclyAvailable(postParam.getInput().getPubliclyAvailable());
			newPost.setDate(postParam.getInput().getDate());
			newPost.setTitle(postParam.getInput().getTitle());
			newPost.setContent(postParam.getInput().getContent());
		}
		postRepository.save(newPost);

		// Let's publish that new post, in case someone subscribed to the subscribeToNewPost GraphQL subscription
		postPublisher.onNext(newPost);

		return newPost;
	}

	@Override
	public List<Post> createPosts(DataFetchingEnvironment dataFetchingEnvironment, List<PostInput> spam) {
		// Actually, this mutation is for sample only. We don't want to implement it !
		// :)
		throw new RuntimeException("Spamming is forbidden");
	}

	@Override
	public Member createMember(DataFetchingEnvironment dataFetchingEnvironment, MemberInput input) {
		Member member = new Member();
		member.setId(rand.nextLong()); // A proper use of ids (with a sequence) would be better for production
		member.setAlias(input.getAlias());
		member.setEmail(input.getEmail());
		member.setName(input.getName());
		member.setType(input.getType());
		return member;
	}

}