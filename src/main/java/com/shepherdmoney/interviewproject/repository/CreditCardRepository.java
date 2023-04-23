package com.shepherdmoney.interviewproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;

/**
 * Crud repository to store credit cards
 */
@Repository("CreditCardRepo")
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    List<CreditCard> findByUser(User user);
    CreditCard findByNumber(String number);
}
