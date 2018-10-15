package com.koczy.kurek.mizera.thesisbrowser.service;

import com.koczy.kurek.mizera.thesisbrowser.entity.Thesis;
import com.koczy.kurek.mizera.thesisbrowser.entity.ThesisDetails;
import com.koczy.kurek.mizera.thesisbrowser.model.ThesisFilters;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ThesisService implements IThesisService {

    @Override
    public List<Thesis> getTheses() {
        return Collections.emptyList();
    }

    @Override
    public List<Thesis> searchTheses(ThesisFilters thesisFilters) {
        return Collections.emptyList();
    }

    @Override
    public ThesisDetails getThesisDetails(Long id) {
        return null;
    }
}
