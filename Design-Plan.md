# ERP-Salesforce-integration
Learning project: Create a simple ERP to store Customers, connect that ERP with a Salesforce playground and add features to manipulate data in various ways.

The design plan:
1. Create a primitive, basic ERP in Java as a backend database to add/store Customer records.
2. Create an API to communicate with the backend.
3. Create a Salesforce playground and put callouts in place to communicate with the ERP API (feature No.1).
4. Add products to the ERP system to be able to create sales offers later on (feature No.4).
5. Add more features to increase the funcionality.

Planned features (subject to change):
1. Allow Salesforce to receive telemetry from a website, creating a new Customer record if one doesnt exist already.
2. Create a trigger that sends the new Customer record data to the ERP system after the new record fields have been filled out.
3. Create a trigger to create a new related Opportunity when a Customer record is added.
4. Introduce a feature to create Quotes based on the Opportunity.
5. Introduce a feature that allows the Quote to be sent to the ERP system.
6. Create a trigger that blocks the ability to edit the Quote after it was sent to the ERP system.
7. Create a trigger that creates an Order when the Quote gets accepted, it will be linked to both the Customer and the Quote.
