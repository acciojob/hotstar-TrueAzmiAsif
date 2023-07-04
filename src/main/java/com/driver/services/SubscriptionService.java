package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription sub=new Subscription();
        Optional<User> optUser=userRepository.findById(subscriptionEntryDto.getUserId());
        User user=optUser.get();
        sub.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        sub.setUser(user);
        sub.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        int totalAmount=0;
        if(subscriptionEntryDto.getSubscriptionType()==SubscriptionType.BASIC)
            totalAmount=500+subscriptionEntryDto.getNoOfScreensRequired()*200;
        else if(subscriptionEntryDto.getSubscriptionType()==SubscriptionType.PRO)
            totalAmount=800+subscriptionEntryDto.getNoOfScreensRequired()*250;
        else
            totalAmount=1000+subscriptionEntryDto.getNoOfScreensRequired()*350;
        sub.setTotalAmountPaid(totalAmount);
        user.setSubscription(sub);
        userRepository.save(user);
        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        int prevAmount=0,newAmount=0;
        User user=userRepository.findById(userId).get();
        if(user.getSubscription().getSubscriptionType()==SubscriptionType.ELITE)
            throw new Exception("Already the best Subscription");
        Subscription sub=user.getSubscription();
        if(sub.getSubscriptionType()==SubscriptionType.BASIC){
            prevAmount=sub.getTotalAmountPaid();
            sub.setSubscriptionType(SubscriptionType.PRO);
            newAmount=800+250*sub.getNoOfScreensSubscribed();
            sub.setTotalAmountPaid(newAmount);
        }
        else{
            prevAmount=sub.getTotalAmountPaid();
            sub.setSubscriptionType(SubscriptionType.ELITE);
            newAmount=1000+350*sub.getNoOfScreensSubscribed();
            sub.setTotalAmountPaid(newAmount);
        }
        return newAmount-prevAmount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subList=subscriptionRepository.findAll();
        int totalrev=0;
        for(Subscription sub: subList){
            totalrev+=sub.getTotalAmountPaid();
        }
        return totalrev;
    }

}
