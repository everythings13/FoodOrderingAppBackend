package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.upgrad.FoodOrderingApp.service.util.MessageKeys.*;

@Service
public class AddressService {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private StateDao stateDao;

    @Autowired
    private CustomerAddressDao customerAddressDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(AddressEntity addressEntity,State stateEntity) throws SaveAddressException, AddressNotFoundException {

        if (!validateAddressEntity(addressEntity)){
            throw new SaveAddressException(SAR_001,NO_FIELD_CAN_BE_EMPTY);
        }

        if(!validateStateID(addressEntity.getStateEntity()))
        {
            throw new AddressNotFoundException(ANF_002,NO_STATE_BY_THIS_ID);
        }


        if(!validatePincode(addressEntity.getPincode())){
            throw new SaveAddressException(SAR_002,INVALID_PINCODE);
        }

        addressEntity.setState(stateEntity);

        AddressEntity savedAddress = addressDao.save(addressEntity);

        return savedAddress;

    }

    public State getStateByUUID (String uuid)throws AddressNotFoundException{
        //Calls getStateByUuid od StateDao to get all the State details.
        State stateEntity = stateDao.getStateByUUID(uuid);
        if(stateEntity == null) {//Checking if its null to return error message.
            throw new AddressNotFoundException(ANF_002, NO_STATE_BY_THIS_ID);
        }
        return  stateEntity;
    }

    public List<AddressEntity> getAllAddress(CustomerEntity customerEntity)
    {
        return customerAddressDao.getAddressByCustomer(customerEntity);
    }

    public List<State> getAllStates() {
        return stateDao.getAllStates();
    }

    private boolean validateStateID(State state) throws AddressNotFoundException {
        if(state == null)
        {
            return false;
        }
        return true;
    }

    private boolean validatePincode(String pincode) {

        try{
            if(pincode.length() == 6)
            {
                Integer.parseInt(pincode);
                return true;
            }
        }
        catch(Exception e)
        {}

        return false;
    }

    private boolean validateAddressEntity(AddressEntity addressEntity) {
        if(addressEntity.getFlatBuilNo() != null && !addressEntity.getFlatBuilNo().isEmpty())
        {
            if(addressEntity.getLocality() != null && !addressEntity.getLocality().isEmpty())
            {
                if(addressEntity.getCity()!= null && !addressEntity.getCity().isEmpty())
                {
                    if(addressEntity.getPincode() != null && !addressEntity.getPincode().isEmpty())
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void validateCustomerAuthEntity(CustomerAuthEntity customerAuthEntity) throws AuthorizationFailedException {
        if (customerAuthEntity == null || customerAuthEntity.getAccessToken() == null) {
            throw new AuthorizationFailedException(ATHR_001, CUSTOMER_IS_NOT_LOGGED_IN);
        } else if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException(ATHR_002, CUSTOMER_IS_LOGGED_OUT);
        } else if (customerAuthEntity.getExpiresAt().compareTo(ZonedDateTime.now()) < 0) {
            throw new AuthorizationFailedException(ATHR_003, SESSION_IS_EXPIRED);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity saveCustomerAddressEntity(CustomerEntity customerEntity, AddressEntity addressEntity){

        CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
        customerAddressEntity.setCustomerEntity(customerEntity);
        customerAddressEntity.setAddressEntity(addressEntity);

        CustomerAddressEntity createdCustomerAddressEntity = customerAddressDao.saveCustomerAddress(customerAddressEntity);
        return createdCustomerAddressEntity;

    }


    public AddressEntity getAddressByUUID(String addressUuid, CustomerEntity customerEntity)throws AuthorizationFailedException,AddressNotFoundException{
        if(addressUuid == null){
            throw new AddressNotFoundException(ANF_005,ADDRESS_CANNOT_BE_EMPTY);
        }

        AddressEntity addressEntity = addressDao.getAddressByUUID(addressUuid);
        if (addressEntity == null){//Checking if null throws corresponding exception.
            throw new AddressNotFoundException(ANF_003,NO_ADDRESS_BY_THIS_ID);
        }

        CustomerAddressEntity customerAddressEntity = customerAddressDao.getCustomerAddressByAddress(addressEntity);
        if(customerAddressEntity == null || customerAddressEntity.getCustomerEntity().getUuid() == customerEntity.getUuid()) {
            return addressEntity;
        } else{
            throw new AuthorizationFailedException(ATHR_004,CUSTOMER_NOT_AUTHORIZED_TO_UPDATE);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(AddressEntity addressEntity) {

        AddressEntity deletedAddressEntity = addressDao.deleteAddress(addressEntity);
        return deletedAddressEntity;
    }
}
