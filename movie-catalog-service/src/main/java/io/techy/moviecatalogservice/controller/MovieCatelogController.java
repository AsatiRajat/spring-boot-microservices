package io.techy.moviecatalogservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.techy.moviecatalogservice.models.CatalogItem;
import io.techy.moviecatalogservice.models.Movie;
import io.techy.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatelogController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatelog(String userId) {

		// get all rated movieId.
		UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId,
				UserRating.class);

		return ratings.getUserRating().stream().map(rating -> {
			// for each movie ID, call movie info service and get
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(),
					Movie.class);
			// Put them all together
			return new CatalogItem(movie.getName(), "Test movie desc", rating.getRating());
		}).collect(Collectors.toList());

	}

}

/*
Movie movie = webClientBuilder.build()
	.get()
	.uri("http://localhost:9002/movies/" + rating.getMovieId())
	.retrieve()
	.bodyToMono(Movie.class)
	.block();
*/