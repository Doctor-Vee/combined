package com.reloadly.paypro.combined.service;

import com.reloadly.paypro.combined.payload.request.UpdateRequest;

public interface UpdateService {

    String processCustomerDetailsUpdate(String accountNumber, UpdateRequest updateRequest);
}
