package com.example.tp_resto.controller;

import com.example.tp_resto.entity.Commande;
import com.example.tp_resto.entity.Facture;
import com.example.tp_resto.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class ControlleurFacture {

    private FactureService factureService;


    @Autowired
    public ControlleurFacture(FactureService factureService) {
        this.factureService = factureService;
    }

        @GetMapping("/facture")
        public ResponseEntity<?> getAllFacture(){
            List<Facture> listFacture = factureService.findAll();
            if (listFacture.isEmpty()) {
                return ResponseEntity.ok("Il n'y a aucune Facture");
            }
            return ResponseEntity.ok(listFacture);
        }



    @GetMapping("/facture/{id}")
    public Facture getFactureById(@PathVariable int id) {
        try {
            return factureService.findById(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Facture introuvable");
        }
    }

    @PostMapping("/factures/commandes")
    public ResponseEntity<?> createFacture(@RequestBody Commande commande) {
        try {
            Facture facture = factureService.createFactureExistingCommande(commande);
            return  ResponseEntity.ok("Creation de la Facture "+facture.getId()+ " a bien ete enregistrer dans la commande numero "
                    +commande.getId());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



//    @PostMapping("/facturest")
//    public ResponseEntity<Facture> createFacture2(@RequestBody Commande commande) {
//        try {
//
//            Facture savedFacture = new Facture();
//            savedFacture.setCommande(commande);
//                    factureService.saveFacture(savedFacture);
//            return ResponseEntity.ok(savedFacture);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }


    @DeleteMapping("delFacture/{id}")
    public ResponseEntity<?> deleteFactureById(@PathVariable int id)  {
            try {
                boolean deleted = factureService.deleteFactureById(id);
                if (deleted) {
                    return ResponseEntity.status(200).body("Facture No " + id + " Supprimée");
                } else {
                    return ResponseEntity.status(400).body("Facture No " + id + " Non Supprimée");
                }
            }catch ( Exception ex) {
                String msg = ex.getMessage();
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Facture Non Supprimée" + msg);
                }
    }


    @PutMapping("/updatefacture/{id}")
    public ResponseEntity<?> updateFactureById(@PathVariable int id, @RequestBody Facture facture)
    {
        if(facture == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Veuillez fourner les modification");

        }else{
            try {
                Facture facture1 = factureService.updateFacture(id, facture);
                if(facture1 !=null){
                    return ResponseEntity.ok(facture1);
                }else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Facture avec Id: "+id+" introuvable");
                }
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Facture non modifier!");
            }
        }

    }
}


