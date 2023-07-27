package com.example.book_novel.service.impl;

import com.example.book_novel.dao.NovelDao;
import com.example.book_novel.model.Novel;


import com.example.book_novel.model.User;
import com.example.book_novel.service.NovelService;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NovelServiceImpl implements NovelService {
    //注入dao
    @Autowired
    private NovelDao novelDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public Boolean insertNovel(Novel novel) {
        //生成ID当前时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String id = now.format(formatter1);
        //当前时间
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter2);
        novel.setOpentime(formatDateTime);
        novel.setId(id);
        System.out.println(novel);
        Novel insertnovel=novelDao.insert(novel);
        if(insertnovel!=null){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Boolean deleteNovelById(String id) {
        try{
            novelDao.deleteById(id);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public Boolean updateNovelById(Novel novel) {
        String id = novel.getId();
        String novelname=novel.getNovelname();
        String novelbrief=novel.getNovelbrief();
        String author=novel.getAuthor();
        String content=novel.getContent();
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("novelname", novelname);
        update.set("novelbrief", novelbrief);
        update.set("author", author);
        update.set("content", content);
        UpdateResult updatenovel = mongoTemplate.updateFirst(query, update, Novel.class);
        if (updatenovel.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Page<Novel> findAll(Pageable pageable) {
        Page<Novel> novelList = novelDao.findAll(pageable);
        return novelList;
    }

    @Override
    public Page<Novel> SearchNovel(String novelname, String author,Integer page,Integer size) {
        Criteria criteria = new Criteria();
        if(StringUtils.isNotBlank(novelname)){
            criteria.and("novelname").regex(".*"+novelname+".*");
        }
        if(StringUtils.isNotBlank(author)){
            criteria.and("author").regex(".*"+author+".*");
        }
//        if (opentime != null && !opentime.isEmpty()) {
//            List<String> opentimeList = new ArrayList<>(opentime);
//            System.out.println(opentimeList);
//            List<Criteria> timeCriteria = opentimeList.stream()
//                    .filter(StringUtils::isNotBlank)
//                    .map(t -> Criteria.where("opentime").is(t))
//                    .collect(Collectors.toList());
//            if (!timeCriteria.isEmpty()) {
//                criteria.andOperator(timeCriteria.toArray(new Criteria[0]));
//            }
//        }
        // 使用 Query 对象封装查询条件
        Query query = new Query();
        query.addCriteria(criteria);

        // 分页查询数据
        Pageable pageable = PageRequest.of(page, size);
        List<Novel> searchnovel = mongoTemplate.find(query.with(pageable), Novel.class);

        // 查询总记录数，将查询结果封装到分页对象中
        Long count = mongoTemplate.count(query, Novel.class);
        return new PageImpl<>(searchnovel, pageable, count);
    }


}
