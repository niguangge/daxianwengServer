package com.niguang.daxianfeng.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.niguang.daxianfeng.model.User;

@Repository
@Mapper
public interface UserMapper {
	@Select("SELECT * FROM user WHERE wx_id = #{wxId};")
	public User getUserByWxId(@Param("wxId") String wxId);

	@Insert("INSERT INTO user(user_id,nick_name,wx_id) VALUES(#{userId},#{nickName},#{wxId});")
	public void addUser(User demo);

	@Update("UPDATE user SET cur_exp=#{exp} ,level=#{level} WHERE wx_id=#{wxId};")
	public int addExp(@Param("wxId") String wxId, @Param("exp") int exp, @Param("level") int level);

	@Update("UPDATE user SET nick_name=#{nickName} ,avatar_url=#{avatarUrl} WHERE wx_id=#{wxId};")
	public int updateNameAndAvatarUrl(@Param("wxId") String wxId, @Param("nickName") String nickName,
			@Param("avatarUrl") String avatarUrl);

}
