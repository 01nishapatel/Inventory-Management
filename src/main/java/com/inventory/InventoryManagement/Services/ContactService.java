package com.inventory.InventoryManagement.Services;

import com.inventory.InventoryManagement.Models.Contact;
import com.inventory.InventoryManagement.Repositories.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public boolean saveContact(Contact contact){
        boolean isSaved=false;
        Contact savedContact=contactRepository.save(contact);
        if(null!=savedContact && savedContact.getContactId()>0){
            isSaved=true;
        }
        return isSaved;
    }
}
