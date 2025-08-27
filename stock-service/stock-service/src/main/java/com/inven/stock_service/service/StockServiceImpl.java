package com.inven.stock_service.service;

import com.inven.stock_service.dto.StockDTO;
import com.inven.stock_service.dto.StockRequestDTO;
import com.inven.stock_service.exception.ResourceNotFoundException;
import com.inven.stock_service.model.Stock;
import com.inven.stock_service.repository.StockRepository;
import com.inven.stock_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final ModelMapper modelMapper;

    @Override
    public StockDTO createStock(StockRequestDTO stockRequestDTO) {
        Stock stock = modelMapper.map(stockRequestDTO, Stock.class);
        stock.setLastUpdated(LocalDateTime.now());
        Stock savedStock = stockRepository.save(stock);
        return convertToDTO(savedStock);
    }

    @Override
    public StockDTO getStockById(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + id));
        return convertToDTO(stock);
    }

    @Override
    public List<StockDTO> getStocksByProductId(Long productId) {
        return stockRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StockDTO updateStock(Long id, StockRequestDTO stockRequestDTO) {
        Stock existingStock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + id));

        modelMapper.map(stockRequestDTO, existingStock);
        existingStock.setLastUpdated(LocalDateTime.now());
        Stock updatedStock = stockRepository.save(existingStock);

        return convertToDTO(updatedStock);
    }

    @Override
    public void deleteStock(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + id));

        stockRepository.delete(stock);
    }

    private StockDTO convertToDTO(Stock stock) {
        return modelMapper.map(stock, StockDTO.class);
    }
}