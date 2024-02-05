package com.example.demo.admin.mapper;

import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.model.MemberParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

   List<MemberDto> selectList(MemberParam parameter);
}
