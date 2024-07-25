package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {

        List<Folder> existingFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames);

        List<Folder> folderList = new ArrayList<>();

        folderNames
                .forEach(folderName -> {
                    if (!folderNameExists(folderName, existingFolderList).get()) {
                        Folder folder = new Folder(folderName, user);
                        folderList.add(folder);
                    } else {
                        throw new IllegalArgumentException("폴더명이 중복되었습니다.");
                    }
                })
        ;

        folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = folderRepository.findAllByUser(user);

        return folderList.stream()
                .map(FolderResponseDto::new)
                .toList();
    }

    private AtomicBoolean folderNameExists(String folderName, List<Folder> existingFolderList) {
        AtomicBoolean result = new AtomicBoolean(false);
        existingFolderList
                .forEach(existingFolder -> {
                    if (existingFolder.getName().equals(folderName)) {
                        result.set(true);
                    }
                });
        return result;
    }

}
