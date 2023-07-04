package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        WebSeries ws=webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(ws!=null){
            throw new Exception("Series is already present");
        }
        Optional<ProductionHouse> optProd=productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        ProductionHouse prodHouse=optProd.get();
        ws=new WebSeries();
        ws.setSeriesName(webSeriesEntryDto.getSeriesName());
        ws.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        ws.setRating(webSeriesEntryDto.getRating());
        ws.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        ws.setProductionHouse(prodHouse);
        prodHouse.getWebSeriesList().add(ws);
        int totalRating=0,count=0;
        for(WebSeries series: prodHouse.getWebSeriesList()){
            totalRating+=series.getRating();count++;
        }
        totalRating+=ws.getRating();
        count++;
        prodHouse.setRatings(((double)totalRating/count));
        ProductionHouse prodResp=productionHouseRepository.save(prodHouse);
        for(WebSeries webSer: prodResp.getWebSeriesList()){
            if(webSer.getSeriesName().equalsIgnoreCase(ws.getSeriesName()))return webSer.getId();
        }
        return ws.getId();
    }

}
