package web.controllers.rest;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import web.model.jpa.entities.Comment;
import web.model.service.CommentService;


@RestController
@RequestMapping(value="/api", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CommentRestController {
	
	@Autowired
	CommentService commentService;
	
	@RequestMapping(value="/comment/{commentId}", method=RequestMethod.GET)
	public Comment getComment(@PathVariable String commentId) throws IOException {
		Long _commentId = Long.parseLong(commentId);
		return commentService.getComment(_commentId);
	}
	
	@RequestMapping(value="/comment/write", method=RequestMethod.PUT)
	public Comment writeComment(HttpServletRequest req, @RequestBody Comment comment) throws IOException {
		return commentService.writeComment(comment);
	}
	
	@RequestMapping(value="/article/{articleId}/comments", method=RequestMethod.GET)
	public List<Comment> getCommentsOfArticle(HttpServletRequest req, @PathVariable String articleId) {
		List<Comment> com = commentService.getComments(articleId);
		return com;
	}

}