// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract InvoiceToken {
    mapping(address => uint256) public balances;

    event Mint(address indexed to, uint256 amount);

    function mint(address to, uint256 amount) public {
        balances[to] += amount;
        emit Mint(to, amount);
    }

    function balanceOf(address user) public view returns (uint256) {
        return balances[user];
    }
}