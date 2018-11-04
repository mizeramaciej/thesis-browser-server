package com.koczy.kurek.mizera.thesisbrowser.hibUtils;

import com.koczy.kurek.mizera.thesisbrowser.entity.Author;
import com.koczy.kurek.mizera.thesisbrowser.entity.Thesis;
import com.koczy.kurek.mizera.thesisbrowser.lda.dataset.BagOfWordsConverter;
import com.koczy.kurek.mizera.thesisbrowser.model.ThesisFilters;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class ThesisDAO implements IThesisDao {

    private static final Logger logger = Logger.getLogger(ThesisDAO.class.getName());
    private static Boolean addAnd = false;

    private IAuthorDao authorDao;

    //DEMO
    private BagOfWordsConverter bagOfWordsConverter;
    //DEMO
    private List<Map<Integer, Integer>> bow = new ArrayList<Map<Integer, Integer>>();
    //DEMO
    private Map<Integer, double[]> similarityVectors = new HashMap<>();

    //DEMO
    @Autowired
    public ThesisDAO(BagOfWordsConverter bagOfWordsConverter, IAuthorDao authorDao) {
        this.bagOfWordsConverter = bagOfWordsConverter;
        this.authorDao = authorDao;
        FileInputStream fileInputStream = null;
        try {
            for (int i = 0; i < 1; i++) {
                fileInputStream = new FileInputStream("parsedPDF/Multiwinner_Voting__A_New_Challenge_for_Social_Choice_Theory.txt");
                bow.add(this.bagOfWordsConverter.convertTxtToBagOfWords(fileInputStream));
                fileInputStream = new FileInputStream("parsedPDF/Comparison_of_association_ratio_in_English_and_Polish_languages.txt");
                bow.add(this.bagOfWordsConverter.convertTxtToBagOfWords(fileInputStream));
                fileInputStream = new FileInputStream("parsedPDF/Comparison_of_association_ratio_in_English_and_Polish_languages.txt");
                bow.add(this.bagOfWordsConverter.convertTxtToBagOfWords(fileInputStream));
                fileInputStream = new FileInputStream("parsedPDF/Predictive_planning_method_for_rescue_robots_in_buildings.txt");
                bow.add(this.bagOfWordsConverter.convertTxtToBagOfWords(fileInputStream));
                fileInputStream = new FileInputStream("parsedPDF/Distance_rationalization_of_voting_rules.txt");
                bow.add(bagOfWordsConverter.convertTxtToBagOfWords(fileInputStream));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //TODO gives nth record of theses
    public Thesis getNthThesis(int n) {
        return new Thesis();
    }

    //TODO get number of documents in database
    public int getNumTheses() {
        return 4;
    }

    //TODO get bow from given thesis
    public Map<Integer, Integer> getThesisBow(int id) {
        return bow.get(id);
    }

    //TODO get list of ids of theses in database
    public List<Integer> getThesesId() {
        return new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(3);
            add(4);
        }};
    }

    //TODO save similarity vector to database
    public void saveSimilarityVector(Integer integer, double[] similarityVector) {
        this.similarityVectors.put(integer, similarityVector);
    }

    //TODO get similarity vector from database
    public double[] getTopicSimilarityVector(int thesisID) {
        return this.similarityVectors.get(thesisID);
    }

    //TODO get thesis
    public Thesis getThesis(int thesisId) {
        return new Thesis();
    }

    //TODO add filter over position in authors list
    @Override
    public List<Thesis> searchTheses(ThesisFilters thesisFilters) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        List<Thesis> thesisList;

        try {
            String sqlQuery = createQuery(thesisFilters);
            thesisList = session.createNativeQuery(sqlQuery, Thesis.class).list();
            for (Thesis thesis : thesisList) {
                Hibernate.initialize(thesis.getRelatedTheses());
                Hibernate.initialize(thesis.getKeyWords());
                Hibernate.initialize(thesis.getAuthors());
            }
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "NullPointerException in thesisFilers. Returning empty list." + e.toString());
            thesisList = new ArrayList<>();
        } catch (NoAuthorException e) {
            logger.log(Level.SEVERE, "Couldn't find author. Returning empty list.");
            thesisList = new ArrayList<>();
        }

        filterDate(thesisList, thesisFilters);

        session.close();

        return thesisList;
    }

    private void filterDate(List<Thesis> thesisList, ThesisFilters thesisFilters) {
        if (Objects.nonNull(thesisFilters.getDateFrom())) {
            thesisList.removeIf(thesis -> thesis.getPublicationDate().after(thesisFilters.getDateFrom()));
        }
        if (Objects.nonNull(thesisFilters.getDateTo())) {
            thesisList.removeIf(thesis -> thesis.getPublicationDate().after(thesisFilters.getDateTo()));
        }
    }

    private String createQuery(ThesisFilters filters) throws NoAuthorException{

        if (!anyFilters(filters)) {
            logger.info("No filters specified. Selecting random 10 theses.");
            return "SELECT * FROM thesis LIMIT 10";
        }

        String query = "SELECT * FROM thesis " +
                "LEFT JOIN author_thesis on thesis.thesisId = author_thesis.thesisId " +
                "LEFT JOIN author on author_thesis.authorId = author.authorId " +
                "LEFT JOIN keywords on keywords.thesisId = thesis.thesisId " +
                "WHERE ";

        addAnd = false;
        query = filterTitle(query, filters.getTitle());
        query = filterAuthor(query, filters.getAuthor());
        query = filterInstitution(query, filters.getInstitution());
        query = filterKeyWords(query, filters.getKeyWords());
        query = filterQuotationNumber(query, filters.getQuotationNumber());

        query = query.concat("GROUP BY thesis.thesisId");
        return query;
    }

    private boolean anyFilters(ThesisFilters filters) {
        return !isBlank(filters.getTitle()) ||
                !isBlank(filters.getAuthor()) ||
                !isBlank(filters.getInstitution()) ||
                !isBlank(filters.getKeyWords()) ||
                !isBlank(filters.getQuotationNumber());
    }

    private String filterTitle(String query, String title) {
        if (!isBlank(title)) {
            query = addAndToWhere(query);
            query = query.concat("thesis.title LIKE '%" + title + "%' ");
            addAnd = true;
        }
        return query;
    }

    private String filterAuthor(String query, String authorName) throws NoAuthorException {
        if (!isBlank(authorName)) {
            Author author = authorDao.getAuthorByName(authorName);
            if (Objects.isNull(author)) {
                throw new NoAuthorException();
            }

            query = addAndToWhere(query);
            query = query.concat("author_thesis.authorId = " + author.getAuthorId() + " ");
            addAnd = true;
        }
        return query;
    }

    private String filterInstitution(String query, String institution) {
        if (!isBlank(institution)) {
            query = addAndToWhere(query);
            query = query.concat("author.university LIKE '%" + institution + "%' ");
            addAnd = true;
        }
        return query;
    }

    private String filterKeyWords(String query, String keyWords) {
        if (!isBlank(keyWords)) {
            query = addAndToWhere(query);
            query = query.concat("keywords.keyWords LIKE '%" + keyWords + "%' ");
            addAnd = true;
        }
        return query;
    }

    private String filterQuotationNumber(String query, Integer quotationNumber) {
        if (!isBlank(quotationNumber)) {
            query = addAndToWhere(query);
            query = query.concat("thesis.citationNo = " + quotationNumber + " ");
            addAnd = true;
        }
        return query;
    }

    private Boolean isBlank(String filter) {
        return Objects.isNull(filter) || filter.trim().isEmpty();
    }

    private Boolean isBlank(Integer filter) {
        return Objects.isNull(filter) || filter < 0;
    }

    private String addAndToWhere(String query) {
        return (addAnd ? query.concat("AND ") : query);
    }
    private class NoAuthorException extends Throwable {
    }
}
