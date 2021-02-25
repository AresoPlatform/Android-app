package com.xw.aschwitkey.entity;

import java.util.List;

public class PoiAddressBean {
    private int status;
    private ResultBean result;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public ResultBean getResult() {
        return result;
    }

    public static class ResultBean {
        private AddressComponent addressComponent;
        private List<Pois> pois;
        private LocationBean location;
        private String formatted_address;

        public void setAddressComponent(AddressComponent addressComponent) {
            this.addressComponent = addressComponent;
        }

        public AddressComponent getAddressComponent() {
            return addressComponent;
        }

        public List<Pois> getPois() {
            return pois;
        }

        public void setPois(List<Pois> pois) {
            this.pois = pois;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }
    }

    public static class AddressComponent {

        private String country;
        private int country_code;
        private String country_code_iso;
        private String country_code_iso2;
        private String province;
        private String city;
        private int city_level;
        private String district;
        private String town;
        private String town_code;
        private String adcode;
        private String street;
        private String street_number;
        private String direction;
        private String distance;

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry_code(int country_code) {
            this.country_code = country_code;
        }

        public int getCountry_code() {
            return country_code;
        }

        public void setCountry_code_iso(String country_code_iso) {
            this.country_code_iso = country_code_iso;
        }

        public String getCountry_code_iso() {
            return country_code_iso;
        }

        public void setCountry_code_iso2(String country_code_iso2) {
            this.country_code_iso2 = country_code_iso2;
        }

        public String getCountry_code_iso2() {
            return country_code_iso2;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getProvince() {
            return province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public void setCity_level(int city_level) {
            this.city_level = city_level;
        }

        public int getCity_level() {
            return city_level;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getDistrict() {
            return district;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getTown() {
            return town;
        }

        public void setTown_code(String town_code) {
            this.town_code = town_code;
        }

        public String getTown_code() {
            return town_code;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet_number(String street_number) {
            this.street_number = street_number;
        }

        public String getStreet_number() {
            return street_number;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getDirection() {
            return direction;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDistance() {
            return distance;
        }

    }

    public static class LocationBean {
        private double lng;
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class Pois {

        private String addr;
        private String cp;
        private String direction;
        private String distance;
        private String name;
        private String poiType;
        private Point point;
        private String tag;
        private String tel;
        private String uid;
        private String zip;

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getAddr() {
            return addr;
        }

        public void setCp(String cp) {
            this.cp = cp;
        }

        public String getCp() {
            return cp;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getDirection() {
            return direction;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDistance() {
            return distance;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setPoiType(String poiType) {
            this.poiType = poiType;
        }

        public String getPoiType() {
            return poiType;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public Point getPoint() {
            return point;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getTel() {
            return tel;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getZip() {
            return zip;
        }
    }

    public static class Point {

        private Double x;
        private Double y;

        public void setX(Double x) {
            this.x = x;
        }

        public Double getX() {
            return x;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public Double getY() {
            return y;
        }

    }
}