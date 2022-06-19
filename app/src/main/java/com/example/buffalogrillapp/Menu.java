package com.example.buffalogrillapp;

public class Menu {

        public final static String ENTRER="Entrees";
        public final static String PLAT="Plats";
        public final static String DESSERT="Desserts";

        public final static String[] MENU_TABs=new String[]{ENTRER,PLAT,DESSERT};

        private int id;
        private String name;
        private String category;

        private String duré_d_vie;
        private String mode_d_emploie;
        private int temperature;


        public Menu(){

        }
        public Menu(int id, String name, String category,String duré_d_vie, String mode_d_emploie, int temperature) {
                this.name = name;
                this.id=id;
                this.duré_d_vie = duré_d_vie;
                this.mode_d_emploie = mode_d_emploie;
                this.category=category;
                this.temperature = temperature;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
        return name;
        }

        public void setName(String name) {
        this.name = name;
        }

        public String getCategory() {
        return category;
        }

        public void setCategory(String category) {
        this.category = category;
        }

        public String getDuré_d_vie() {
            return duré_d_vie;
        }

        public void setDuré_d_vie(String duré_d_vie) {
            this.duré_d_vie = duré_d_vie;
        }

        public String getMode_d_emploie() {
            return mode_d_emploie;
        }

        public void setMode_d_emploie(String mode_d_emploie) {
            this.mode_d_emploie = mode_d_emploie;
        }


        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }
}
