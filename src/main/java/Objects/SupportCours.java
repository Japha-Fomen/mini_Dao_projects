package Objects;

public class SupportCours {

        private int idSupport;
        private int idCoursOffert;
        private String chemin;

        public SupportCours(int idSupport, int idCoursOffert, String chemin) {
            this.idSupport = idSupport;
            this.idCoursOffert = idCoursOffert;
            this.chemin = chemin;
        }

        public SupportCours(int idCoursOffert, String chemin) {
            this.idCoursOffert = idCoursOffert;
            this.chemin = chemin;
        }

        public int getIdSupport() { return idSupport; }
        public int getIdCoursOffert() { return idCoursOffert; }
        public String getChemin() { return chemin; }

        public void setChemin(String chemin) { this.chemin = chemin; }


}
