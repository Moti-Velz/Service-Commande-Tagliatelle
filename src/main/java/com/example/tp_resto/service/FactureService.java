package com.example.tp_resto.service;

import com.example.tp_resto.entity.Facture;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FactureService {

    Facture saveFacture(Facture facture);

    Facture updateFacture(int id, Facture facture);

    Facture findById(int id);

    List<Facture> findAll();

    Facture findByDate(Date date);

    void deleteFacture(Facture facture);

    void deleteFactureById(int id);
}
