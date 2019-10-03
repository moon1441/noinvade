package com.electric.noinvade.controller;

import com.electric.noinvade.bo.Power;
import com.electric.noinvade.repositry.PowerRepository;
import com.electric.noinvade.vo.AllPowerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/main")
public class MainController {

    @Autowired
    private PowerRepository powerRepository;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public AllPowerVO getAllPower(){
        AllPowerVO vo=new AllPowerVO();
        List<Power> powers = powerRepository.getPower();
        vo.setPa(powers.get(0).getPa());
        return vo;
    }
}
