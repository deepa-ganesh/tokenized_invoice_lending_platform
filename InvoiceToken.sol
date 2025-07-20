// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract InvoiceToken {
    uint256 public nextTokenId;
    
    struct Invoice {
        uint256 tokenId;
        uint256 invoiceId;
        address issuer;
        uint256 amount;
        uint256 timestamp;
    }

    mapping(uint256 => Invoice) public invoices;

    event InvoiceTokenized(
        uint256 tokenId,
        uint256 invoiceId,
        address indexed issuer,
        uint256 amount
    );

    function tokenizeInvoice(uint256 invoiceId, uint256 amount) public returns (uint256) {
        uint256 tokenId = nextTokenId++;
        invoices[tokenId] = Invoice(tokenId, invoiceId, msg.sender, amount, block.timestamp);
        emit InvoiceTokenized(tokenId, invoiceId, msg.sender, amount);
        return tokenId;
    }

    function getInvoice(uint256 tokenId) public view returns (Invoice memory) {
        return invoices[tokenId];
    }
}
