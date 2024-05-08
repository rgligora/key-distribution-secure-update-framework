package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface DeviceMapper {

    Device requestToModel(CreateDeviceRequest request);

    @Mapping(target = "companyId", source = "company.companyId")
    @Mapping(target = "modelId", source = "model.modelId")
    DeviceDto modelToDto(Device device);

    List<DeviceDto> modelToDtos(List<Device> devices);

}
