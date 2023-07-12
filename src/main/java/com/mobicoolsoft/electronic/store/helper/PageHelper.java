package com.mobicoolsoft.electronic.store.helper;

import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.entity.User;
import com.mobicoolsoft.electronic.store.service.UserServiceI;
import com.mobicoolsoft.electronic.store.service.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PageHelper {

    /**
     * <U> source entity type
     * <V> target dto type
     * @param dtoType<V> act as a target class of type<V> for mapping
     * @param page<U> object of page containing source entity class of type <U>
     * @return PageResponse<V> object of dtoType <V>
     */

    public static <U,V>PageResponse<V> getPageResponse(Page<U> page, Class<V> dtoType){

        List<U> entityList = page.getContent();
        List<V> dtoList = entityList.stream().map((entity) -> new ModelMapper().map(entity, dtoType)).collect(Collectors.toList());
        PageResponse<V> pageResponse = new PageResponse();
        pageResponse.setContent(dtoList);
        pageResponse.setPageNumber(page.getNumber()+1);
        pageResponse.setPageSize(page.getSize());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setLastPage(page.isLast());
        return pageResponse;
    }

}
