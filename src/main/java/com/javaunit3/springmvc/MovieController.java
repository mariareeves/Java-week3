package com.javaunit3.springmvc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private final BestMovieService bestMovieService;

    @Autowired
    private SessionFactory sessionFactory;

    public MovieController(BestMovieService bestMovieService) {
        this.bestMovieService = bestMovieService;
    }

    @RequestMapping("/")
    public String getIndexPage(){
        return "index";
    }
    @RequestMapping("/bestMovie")
    public String getBestMoviePage(Model model){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();
        movieEntityList.sort(Comparator.comparing(movieEntity -> movieEntity.getVotes(());

        MovieEntity movieWithMostVotes = movieEntityList.get(movieEntityList.size() - 1);
        List<String> voterNames = new ArrayList<>();

        for (VoteEntity vote: movieWithMostVotes.getVotes())
        {
            voterNames.add(vote.getVoterName());
        }

        String voterNamesList = String.join(",", voterNames);

        model.addAttribute("bestMovie", movieWithMostVotes.getTitle());
        model.addAttribute("bestMovieVoters", voterNamesList);

        session.getTransaction().commit();

        return "bestMovie";

    }

    @RequestMapping("/voteForBestMovieForm")
    public String voteForBestMovieFormPage(Model model) {

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();
        session.getTransaction().commit();
        model.addAttribute("movies", movieEntityList);

        return "voteForTheBestMovie";
    }

    @RequestMapping("/voteForBestMovie")
    public String voteForBestMovie(HttpServletRequest request, Model model) {
        String movieId= request.getParameter("movieId");
        String voterName = request.getParameter("voterName");

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        MovieEntity movieEntity = (MovieEntity) session.get(MovieEntity.class, Integer.parseInt(movieId));
        VoteEntity newVote = new VoteEntity();
        newVote.setVoterName(voterName);

        session.update(movieEntity);

        session.getTransaction().commit();


        return "voteForTheBestMovie";
    }

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/addMovieForm")
    public String addMovieForm(){
        return "addMovie";
    }

}
