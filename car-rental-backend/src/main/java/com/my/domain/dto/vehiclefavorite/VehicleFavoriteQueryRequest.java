package com.my.domain.dto.vehiclefavorite;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleFavoriteQueryRequest extends PageRequest implements Serializable {

    String searchText;
}
