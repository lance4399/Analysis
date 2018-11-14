package utils;
import com.alibaba.fastjson.JSON;

/**
 * @Author: xiliang
 * @Create: 2018/8/1 22:11
 */
public class Bonus {
    private String type;
    private String sms_id;
    private String email_template_id;
    private String mp_media_id;
    private String operator_passport;
    private String operator_name;
    private String old_level;
    private String new_level;
    private String old_mp_channel_id;
    private String new_mp_channel_id;
    private String mp_profile_id;
    private String profile_reaction;
    private String create_time;

    public Bonus() { }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSms_id() {
        return sms_id;
    }

    public void setSms_id(String sms_id) {
        this.sms_id = sms_id;
    }

    public String getEmail_template_id() {
        return email_template_id;
    }

    public void setEmail_template_id(String email_template_id) {
        this.email_template_id = email_template_id;
    }

    public String getMp_media_id() {
        return mp_media_id;
    }

    public void setMp_media_id(String mp_media_id) {
        this.mp_media_id = mp_media_id;
    }

    public String getOperator_passport() {
        return operator_passport;
    }

    public void setOperator_passport(String operator_passport) {
        this.operator_passport = operator_passport;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getOld_level() {
        return old_level;
    }

    public void setOld_level(String old_level) {
        this.old_level = old_level;
    }

    public String getNew_level() {
        return new_level;
    }

    public void setNew_level(String new_level) {
        this.new_level = new_level;
    }

    public String getOld_mp_channel_id() {
        return old_mp_channel_id;
    }

    public void setOld_mp_channel_id(String old_mp_channel_id) {
        this.old_mp_channel_id = old_mp_channel_id;
    }

    public String getNew_mp_channel_id() {
        return new_mp_channel_id;
    }

    public void setNew_mp_channel_id(String new_mp_channel_id) {
        this.new_mp_channel_id = new_mp_channel_id;
    }

    public String getMp_profile_id() {
        return mp_profile_id;
    }

    public void setMp_profile_id(String mp_profile_id) {
        this.mp_profile_id = mp_profile_id;
    }

    public String getProfile_reaction() {
        return profile_reaction;
    }

    public void setProfile_reaction(String profile_reaction) {
        this.profile_reaction = profile_reaction;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
