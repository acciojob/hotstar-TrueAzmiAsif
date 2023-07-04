package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User us=userRepository.save(user);
        return us.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        List<WebSeries> webSeriesList=webSeriesRepository.findAll();
        Optional<User> optUser=userRepository.findById(userId);
        User user=optUser.get();
        int count=0;
        for(WebSeries ws: webSeriesList){
            if((ws.getAgeLimit()<user.getAge()) && ws.getSubscriptionType()==user.getSubscription().getSubscriptionType()){
                count++;
            }
        }
        return count;
    }


}
