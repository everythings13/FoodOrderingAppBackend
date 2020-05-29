package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired private AddressService addressService;

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/address",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(
            @RequestHeader("authorization") final String accessTokenauthorization,
            final SaveAddressRequest saveAddressRequest) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setFlatBuildingName(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setUuid(UUID.randomUUID().toString());

        AddressEntity address = addressService.saveAddress(addressEntity,accessTokenauthorization,saveAddressRequest.getStateUuid());

        SaveAddressResponse saveAddressResponse = new SaveAddressResponse();
        saveAddressResponse.setId(address.getUuid());
        saveAddressResponse.setStatus("ADDRESS SUCCESSFULLY REGISTERED");

        return new ResponseEntity<>(saveAddressResponse, HttpStatus.CREATED);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/address/customer",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllSavedAddress(@RequestHeader("authorization") final String accessTokenauthorization)
            throws AuthorizationFailedException {

        List<AddressEntity> addressEntityList = addressService.getAllSavedAddress(accessTokenauthorization);

        List<AddressList> addressLists = new ArrayList<>();

        addressEntityList.stream()
                .forEach(
                        addressEntity -> {
                            AddressList addressList = new AddressList();
                            addressList.setCity(addressEntity.getCity());
                            addressList.setFlatBuildingName(addressEntity.getFlatBuildingName());
                            addressList.setLocality(addressEntity.getLocality());
                            addressList.setPincode(addressEntity.getPincode());
                            addressList.setId(UUID.fromString(addressEntity.getUuid()));

                            AddressListState addressListState = new AddressListState();
                            addressListState.setId(UUID.fromString(addressEntity.getStateEntity().getUuid()));
                            addressListState.setStateName(addressEntity.getStateEntity().getStateName());

                            addressList.setState(addressListState);

                            addressLists.add(addressList);
                        }
                );

        AddressListResponse addressListResponse = new AddressListResponse();
        addressListResponse.setAddresses(addressLists);

        return new ResponseEntity<>(addressListResponse, HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/states",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        List<StateEntity> stateEntityList = addressService.getAllStates();

        List<StatesList> statesLists = new ArrayList<>();
        stateEntityList.stream()
                .forEach(
                        stateEntity -> {
                            StatesList state = new StatesList();
                            state.setId(UUID.fromString(stateEntity.getUuid()));
                            state.setStateName(stateEntity.getStateName());
                            statesLists.add(state);
                        }
                );

        StatesListResponse statesListResponse = new StatesListResponse();
        statesListResponse.setStates(statesLists);

        return new ResponseEntity<>(statesListResponse, HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/address/{address_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@RequestHeader("authorization") final String accessTokenauthorization,
                                                               @PathVariable("address_id") final String addressId) throws AuthorizationFailedException, AddressNotFoundException {
        AddressEntity addressEntity = addressService.deleteAddress(accessTokenauthorization,addressId);
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse();
        deleteAddressResponse.setId(UUID.fromString(addressEntity.getUuid()));
        deleteAddressResponse.setStatus("ADDRESS DELETED SUCCESSFULLY");

        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }
}
