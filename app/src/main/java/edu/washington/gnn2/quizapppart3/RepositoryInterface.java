package edu.washington.gnn2.quizapppart3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Gabby on 2/16/2015.
 */
public interface RepositoryInterface {
    //get result
    //gettopic
    public HashMap<String,Topic> createRepo(ArrayList<String> topics,HashMap<String, ArrayList<String>> questions, HashMap<String, ArrayList<String>> answers, HashMap<String, HashMap<Integer, ArrayList<String>>> possibleAnswers);
    public HashMap<String, Topic> getRepo();
    public Topic getTopic(String topic);
    public Set<String> getRepoKeySet();
    public HashMap<String,Topic> addTopicQuestion(String topic, String desc, String question, String answer, ArrayList<String> answers);
}
