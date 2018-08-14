package net.lueying.s_image.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 设备首页
 */
public class DeviceIndex {

    private List<DevBean> Dev;
    private List<GrpBean> Grp;

    public List<DevBean> getDev() {
        return Dev;
    }

    public void setDev(List<DevBean> Dev) {
        this.Dev = Dev;
    }

    public List<GrpBean> getGrp() {
        return Grp;
    }

    public void setGrp(List<GrpBean> Grp) {
        this.Grp = Grp;
    }

    public static class DevBean {
        /**
         * id : 10000
         * device_name : 起个新名字
         * device_model : FDJEK156
         * mac : 35:89:78:45:12
         * is_status : 0
         * shareTo : 我没有名字4
         */

        private int id;
        private String device_name;
        private String device_model;
        private String mac;
        private int is_status;
        private String shareTo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public String getDevice_model() {
            return device_model;
        }

        public void setDevice_model(String device_model) {
            this.device_model = device_model;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getIs_status() {
            return is_status;
        }

        public void setIs_status(int is_status) {
            this.is_status = is_status;
        }

        public String getShareTo() {
            return shareTo;
        }

        public void setShareTo(String shareTo) {
            this.shareTo = shareTo;
        }
    }

    public static class GrpBean {
        /**
         * id : 79
         * group_id : 20180615765145
         * uid : 94
         * lat : 40.002617
         * lng : 116.783225
         * group_owner : 0
         * private : 2
         * created_at : 2018-06-15 09:47:51
         * count_down :
         */

        private int id;
        private String group_id;
        private int uid;
        private String lat;
        private String lng;
        private int group_owner;
        @SerializedName("private")
        private int privateX;
        private String created_at;
        private String count_down;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public int getGroup_owner() {
            return group_owner;
        }

        public void setGroup_owner(int group_owner) {
            this.group_owner = group_owner;
        }

        public int getPrivateX() {
            return privateX;
        }

        public void setPrivateX(int privateX) {
            this.privateX = privateX;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCount_down() {
            return count_down;
        }

        public void setCount_down(String count_down) {
            this.count_down = count_down;
        }
    }
}
