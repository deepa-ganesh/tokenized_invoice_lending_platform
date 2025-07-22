const InvoiceToken = artifacts.require("InvoiceToken");

module.exports = async function (deployer) {
  await deployer.deploy(InvoiceToken);
};
