package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public AddressEntity saveAddress(AddressEntity addressEntity, String accessToken, String stateUUID)
            throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {

        validateCustomerAuthEntity(accessToken);

        StateEntity state = stateDao.getStateById(Integer.parseInt(stateUUID));

        if(!validateStateID(state))
        {
            throw new AddressNotFoundException(ANF_002,NO_STATE_BY_THIS_ID);
        }

        addressEntity.setStateEntity(state);

        if(!validateAddressEntity(addressEntity))
        {
            throw new SaveAddressException(SAR_001,NO_FIELD_CAN_BE_EMPTY);
        }
        if(!validatePincode(addressEntity.getPincode()))
        {
            throw new SaveAddressException(SAR_002,INVALID_PINCODE);
        }

        return addressDao.save(addressEntity);
    }

    public List<AddressEntity> getAllSavedAddress(final String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthEntityByAccessToken(accessToken);
        validateCustomerAuthEntity(customerAuthEntity);
        CustomerEntity customerEntity = customerAuthEntity.getCustomerEntity();

        return customerAddressDao.getAddressByCustomer(customerEntity);
    }

    public List<StateEntity> getAllStates() {
        return stateDao.getAllStates();
    }

    private boolean validateStateID(StateEntity state) throws AddressNotFoundException {
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
        if(addressEntity.getFlatBuildingName() != null && !addressEntity.getFlatBuildingName().isEmpty())
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

    /**
     * This method will validate the access token provided and if access token is valid and user is logged in then provide the CustomerAuthEntity object
     * @param accessToken
     * @return User Entity after validating access token and logout time
     * @throws AuthorizationFailedException
     */
    private void validateCustomerAuthEntity(String accessToken)
            throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthEntityByAccessToken(accessToken);
        validateCustomerAuthEntity(customerAuthEntity);
    }

    private void validateCustomerAuthEntity(CustomerAuthEntity customerAuthEntity) throws AuthorizationFailedException {
        if (customerAuthEntity == null || customerAuthEntity.getAccessToken() == null) {
            throw new AuthorizationFailedException(ATHR_001, CUSTOMER_IS_NOT_LOGGED_IN);
        } else if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException(ATHR_002, CUSTOMER_IS_LOGGED_OUT);
        } else if (customerAuthEntity.getExpiresAt().compareTo(LocalDateTime.now()) < 0) {
            throw new AuthorizationFailedException(ATHR_003, SESSION_IS_EXPIRED);
        }
    }
}
