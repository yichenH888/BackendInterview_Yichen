package com.shepherdmoney.interviewproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;

@RestController
public class CreditCardController {

    // TODO: wire in CreditCard repository here (~1 line)
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private UserRepository userRepository;
    


    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        // TODO: Create a credit card entity, and then associate that credit card with
        // user with given userId
        // Return 200 OK with the credit card id if the user exists and credit card is
        // successfully associated with the user
        // Return other appropriate response code for other exception cases
        // Do not worry about validating the card number, assume card number could be
        // any arbitrary format and length
        try {
            
           
            User user = userRepository.findById(payload.getUserId());
            if(user == null) {
                return ResponseEntity.badRequest().build();
            }
            CreditCard creditCard = new com.shepherdmoney.interviewproject.model.CreditCard();
            creditCard.setIssuanceBank(payload.getCardIssuanceBank());
            creditCard.setNumber(payload.getCardNumber());
            creditCard.setUser(user);
            creditCardRepository.save(creditCard);
            return ResponseEntity.ok(creditCard.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        // TODO: return a list of all credit card associated with the given userId,
        // using CreditCardView class
        // if the user has no credit card, return empty list, never return null
        // return 400 Bad Request if the user does not exist
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            List<CreditCard> creditCards = creditCardRepository.findByUser(user);
            List<CreditCardView> creditCardViews = new ArrayList<>();
            for (CreditCard creditCard : creditCards) {
                CreditCardView creditCardView = CreditCardView.builder().issuanceBank(creditCard.getIssuanceBank())
                        .number(creditCard.getNumber()).build();
                creditCardViews.add(creditCardView);
            }

            return ResponseEntity.ok(creditCardViews);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // TODO: Given a credit card number, efficiently find whether there is a user
        // associated with the credit card
        // If so, return the user id in a 200 OK response. If no such user exists,
        // return 400 Bad Request
        try {
        CreditCard creditCard = creditCardRepository.findByNumber(creditCardNumber);
            return ResponseEntity.ok(creditCard.getUser().getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/credit-card:update-balance")
     public ResponseEntity<Integer> postMethodName(@RequestBody UpdateBalancePayload[]
     payload) {
     // TODO: Given a list of transactions, update credit cards' balance history.
     // For example: if today is 4/12, a credit card's balanceHistory is [{date:
     // 4/12, balance: 110}, {date: 4/10, balance: 100}],
     // Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
     // [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10,
     // balance: 110}]
     // Return 200 OK if update is done and successful, 400 Bad Request if the
     // given card number
     // is not associated with a card.
     try{
         for (UpdateBalancePayload updateBalancePayload : payload) {
             
             String cardNumber = updateBalancePayload.getCreditCardNumber();
             CreditCard creditCard = creditCardRepository.findByNumber(cardNumber);
             List<BalanceHistory> balanceHistory = new ArrayList<>();
             balanceHistory = creditCard.getBalanceHistory();
             BalanceHistory newBalanceHistory = new BalanceHistory();
             newBalanceHistory.setBalance(updateBalancePayload.getTransactionAmount()+balanceHistory.get(0).getBalance());
             newBalanceHistory.setDate(updateBalancePayload.getTransactionTime());
             newBalanceHistory.setCreditCard(creditCard);
             System.out.println(newBalanceHistory);
             creditCard.setBalanceHistory(balanceHistory);
             creditCardRepository.save(creditCard);
         }
            return ResponseEntity.ok().build();
     }
     catch(Exception e){
         return ResponseEntity.badRequest().build();
     }
 }
}
