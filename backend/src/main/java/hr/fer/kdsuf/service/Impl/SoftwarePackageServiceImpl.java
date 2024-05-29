package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.SoftwareNotFoundException;
import hr.fer.kdsuf.exception.exceptions.SoftwarePackageNotFoundException;
import hr.fer.kdsuf.mapper.SoftwarePackageMapper;
import hr.fer.kdsuf.model.domain.*;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.CreateSoftwarePackageRequest;
import hr.fer.kdsuf.repository.*;
import hr.fer.kdsuf.service.SoftwarePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoftwarePackageServiceImpl implements SoftwarePackageService{
    @Autowired
    private SoftwarePackageRepository softwarePackageRepository;

    @Autowired
    private SoftwarePackageMapper softwarePackageMapper;

    @Autowired
    private SoftwareRepository softwareRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private VaultSecretServiceImpl vaultSecretService;

    @Override
    public SoftwarePackageDto createSoftwarePackage(CreateSoftwarePackageRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company with id: '" + request.getCompanyId() + "' does not exist!"));
        SoftwarePackage softwarePackage = softwarePackageMapper.requestToModel(request);

        List<Software> includedSoftware = request.getIncludedSoftwareIds().stream()
                .map(id -> softwareRepository.findById(id).orElse(null))
                .collect(Collectors.toList());

        List<Model> models = request.getModelIds().stream()
                .map(id -> modelRepository.findById(id).orElse(null))
                .collect(Collectors.toList());

        softwarePackage.setCreationDate(LocalDate.now());
        softwarePackage.setCompany(company);
        softwarePackage.setIncludedSoftware(includedSoftware);
        softwarePackage.setModels(models);
        softwarePackage.setStatus(PackageStatus.AVAILABLE);

        SoftwarePackageDto softwarePackageDto = softwarePackageMapper.modelToDto(softwarePackage);
        String dataToSign = softwarePackageDto.toString();
        String signature = vaultSecretService.signData("software-signing-key", dataToSign.getBytes(StandardCharsets.UTF_8));
        vaultSecretService.storeSignature(softwarePackage.getSoftwarePackageId(), signature);


        company.addSoftwarePackage(softwarePackage);
        companyRepository.save(company);
        softwarePackageRepository.save(softwarePackage);

        for (Model model : models) {
            if (model != null) {
                List<Device> devices = model.getDevices();
                for (Device device : devices) {
                    device.setStatus(DeviceStatus.UPDATE_PENDING);
                    deviceRepository.save(device);
                }
            }
        }
        return softwarePackageMapper.modelToDto(softwarePackage);
    }

    @Override
    public SoftwarePackageDto retrieveSoftwarePackage(String id) {
        SoftwarePackage softwarePackage = softwarePackageRepository.findById(id).orElseThrow(() -> new SoftwarePackageNotFoundException(id));
        return softwarePackageMapper.modelToDto(softwarePackage);
    }

    @Override
    public List<SoftwarePackageDto> retrieveSoftwarePackages(String companyId) {
        List<SoftwarePackage> softwarePackages;
        if(companyId != null){
            softwarePackages = softwarePackageRepository.findSoftwarePackageByCompanyCompanyId(companyId);
        }else{
            softwarePackages = softwarePackageRepository.findAll();
        }
        return softwarePackageMapper.modelToDtos(softwarePackages);
    }

    @Override
    public void deleteSoftwarePackage(String id) {
        boolean softwarePackageExists = softwarePackageRepository.existsById(id);
        if (!softwarePackageExists){
            throw new SoftwareNotFoundException(id);
        }
        vaultSecretService.deleteSignature(id);
        softwarePackageRepository.deleteById(id);
    }
}
