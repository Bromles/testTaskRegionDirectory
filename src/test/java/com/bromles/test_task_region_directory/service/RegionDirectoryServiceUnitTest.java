package com.bromles.test_task_region_directory.service;

import com.bromles.test_task_region_directory.repository.IRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class RegionDirectoryServiceUnitTest {
    @Autowired
    private IRegionDirectoryService regionDirectoryService;

    @MockBean
    private IRegionRepository regionRepository;

}
