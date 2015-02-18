package edu.washington.gnn2.quizapppart3;

import android.content.ServiceConnection;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Gabby on 2/16/2015.
 */
public class Repository implements RepositoryInterface {
    /**
    private static HashMap<String, ArrayList<String>> questions; //
    private static HashMap<String, ArrayList<String>> answers;
    private static HashMap<String, HashMap<Integer, ArrayList<String>>> possibleAnswers;
    private static ArrayList<String> quizes;
    private static ArrayList<String> topicQ;
     */

    private static HashMap<String,Topic> repo;

    public Repository(){
     /**   quizes = addTopics();
        topicQ = addQuestions();
        questions = initQuestions(quizes);
        answers = initAnswers(quizes);
        possibleAnswers = setPossibleAnswers();
      */
        repo = new HashMap<String, Topic>();


    }



    public HashMap<String, Topic> getRepo() {
        return this.repo;
    }

    /**
    private void setRepo(HashMap<String, Topic> repo) {
        this.repo = repo;
    }
*/
    public HashMap<String,Topic> createRepo(ArrayList<String> topics,HashMap<String, ArrayList<String>> questions, HashMap<String, ArrayList<String>> answers, HashMap<String, HashMap<Integer, ArrayList<String>>> possibleAnswers){
        //HashMap<String, Topic> result = new HashMap<String, Topic>();
        int i = 0;
        for(String topic: topics){
            if(!repo.containsKey(topic)){
                Topic t = new Topic (topic, "short blah", "blahahahaha hiiiiii blahshshahahahaha");
                ArrayList<Question> q = setQuestions(i, topic, questions, answers, possibleAnswers);
                t.setQuizQuestions(q);
                repo.put(topic, t);
            }
            i++;
        }
        return repo;
    }

    private static ArrayList<Question> setQuestions(int index, String topic, HashMap<String, ArrayList<String>> questions,HashMap<String, ArrayList<String>> answers,HashMap<String, HashMap<Integer, ArrayList<String>>> possibleAnswers ){
        ArrayList<Question> q = new ArrayList<Question>();
        ArrayList<String> questionArray = questions.get(topic);
        for(int i = 0; i < questionArray.size(); i++){
            String question = questionArray.get(i);
            Question qu = new Question(question);
            ArrayList<String> possible = possibleAnswers.get(topic).get(i);
            String answer = answers.get(topic).get(i);
            int a = possible.indexOf(answer);
            qu.setPossibleAnswers(possible);
            qu.setCorrectAnswer(a);
            q.add(qu);
        }
        return q;
    }

    /**
     *
     * @param topic
     * @return
     */
    public Topic getTopic(String topic){
        return repo.get(topic);
    }

    public Set<String> getRepoKeySet(){
        return repo.keySet();
    }
}
