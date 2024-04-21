package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.SoftwareNotFoundException;
import hr.fer.kdsuf.mapper.SoftwareMapper;
import hr.fer.kdsuf.model.domain.Software;
import hr.fer.kdsuf.model.dto.SoftwareDto;
import hr.fer.kdsuf.model.request.CreateSoftwareRequest;
import hr.fer.kdsuf.repository.SoftwareRepository;
import hr.fer.kdsuf.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoftwareServiceImpl implements SoftwareService {

    @Autowired
    SoftwareMapper softwareMapper;

    @Autowired
    SoftwareRepository softwareRepository;

    @Override
    public SoftwareDto createSoftware(CreateSoftwareRequest request) {
        Software software = softwareMapper.requestToModel(request);
        /*Set fileSize and checksum calculated based on the software*/
        softwareRepository.save(software);
        return softwareMapper.modelToDto(software);
    }

    @Override
    public SoftwareDto retrieveSoftware(String id) {
        Software software = softwareRepository.findById(id).orElseThrow(() -> new SoftwareNotFoundException(id));
        return softwareMapper.modelToDto(software);
    }

    @Override
    public void deleteSoftware(String id) {
        boolean softwareExists = softwareRepository.existsById(id);
        if (!softwareExists){
            throw new SoftwareNotFoundException(id);
        }
        softwareRepository.deleteById(id);
    }
}
