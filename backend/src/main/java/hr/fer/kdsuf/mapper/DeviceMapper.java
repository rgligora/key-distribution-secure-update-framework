package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface DeviceMapper {

    @Mapping(target = "deviceId", expression = "java(java.util.UUID.randomUUID().toString())")
    Device requestToModel(CreateDeviceRequest request);

    DeviceDto modelToDto(Device device);

    List<DeviceDto> modelToDtos(List<Device> devices);

}
