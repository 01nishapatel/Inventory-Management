package com.inventory.InventoryManagement.Services;

import com.inventory.InventoryManagement.ImageUtil.ImagesUtil;
import com.inventory.InventoryManagement.Models.CompanyDetails;
import com.inventory.InventoryManagement.Repositary.CompanyDetailsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CompanyLogoService {
    @Autowired
    private CompanyDetailsRepository LogoRepo;

    public CompanyDetails uploadImage(MultipartFile file,CompanyDetails cImage) throws IOException {
//        CompanyDetails pImage = new CompanyDetails();
        cImage.setLogoName(file.getOriginalFilename());
        cImage.setLogoType(file.getContentType());
        cImage.setCompanyLogo(ImagesUtil.compressImage(file.getBytes()));
        return LogoRepo.save(cImage);
    }

//    public byte[] downloadImage(String fileName){
//        Optional<CompanyDetails> imageData = LogoRepo.findBylogoName(fileName);
//        return ImagesUtil.decompressImage(imageData.get().getCompanyLogo());
//    }
}
