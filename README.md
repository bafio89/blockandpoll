
# Block'n'Poll
[Block'n'Poll]((https://blockandpoll.herokuapp.com/)) is a blockchain polling web application that allows users to easily create permissionless polls and vote on Algorand blockchain.

## Why voting on blockchain?
Although voting processes require particular attention to privacy, especially in the government sphere, simple polls are instead a good use case for blockchain technology, which ensures a public, verifiable, tamper-proof and uncensored global voting process. Furthermore, a blockchain polling system enables use cases in which response incentive mechanisms can be built. Even if the web app hosting domain is shut down, anyone still have the possibility to express his/her vote just using an Algorand node.

## Algorand Java Web App
This web application aims to show **how easily integrate Algorand into a full-stack Java web application** through [Algorand Java SDK](https://developer.algorand.org/docs/reference/sdks/#java). Polls are implemented as [Algorand Stateful Smart Contract](https://developer.algorand.org/docs/features/asc1/stateful/): their [TEAL](https://developer.algorand.org/docs/features/asc1/teal/) logic validates both polls creation, opt-in and voting, **avoiding double-voting** for a same account. The Stateful ASC1 TEAL logic is derived from the [Permissionless Voting Solution](https://developer.algorand.org/solutions/example-permissionless-voting-stateful-smart-contract-application/) published on Algorand Developer Portal. Block'n'Poll expands the original TEAL source code in order to allow the creation of polls with more than two options.

## Architecture
The web application is based on Java, integrating **Spring Boot** and **Algorand Java SDK** in order to interact with the blockchain. The back-end of the application has been developed following **[TDD](https://en.wikipedia.org/wiki/Test-driven_development) approach** and **[Hexagonal Architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)) principles**. A **PostreSQL** data-base is used just to speed up polls retrieving operations, while the **blockchain is always the real source of truth**. The front-end has been developed using **ReactJS**.

## Block'n'Poll use cases
Block'n'Poll web-app implements the following use-cases:

1. **Showing existing polls:** in the landing page a dashboard shows created polls in small frames, highlighting basic polls' information. By clicking on any frame the user can open the poll's page with more details.

2. **Poll creation:** the user can create a poll, entering the required information like: poll question, poll name and poll options, specifying the time frame in which it is allowed to opt-in and vote for the poll. In this first version of the web-app, users interaction happens on the front-end through their **mnemonic key** (which is not stored in any way by the web app), signing required transactions to create poll's Smart Contract application on blockchain.

3. **Poll visualisation:** the user can open a poll displayed in the landing page in order to get a detailed view of poll's information, like: poll name, poll question, opt-in time frame, vote time frame, number of Algorand accounts subscribed to the poll and the number of votes received by each option. An histogram chart shows the votes summary.

4. **Poll Opt-in**: opt-in action is performed by the user entering the mnemonic key and clicking the **opt-in button**, if the opt-in phase has not expired yet and the user has not yet opt-in this poll, the application returns a green confirmation message. Otherwise a red error message is shown.

5. **Poll Vote:** voting action is performed by the user selecting an option, entering the mnemonic key and clicking on the **vote button** in order to express the vote. If the user has not voted for the poll yet and the voting phase is still open, the application returns a green confirmation message. In case the user votes before the opt-in, Block'n'Poll try to automatically perform opt-in on user behalf: if opt-in phase is still open, Block'n'Poll performs opt-in and vote actions in sequence.

## Managing your polls
[Algodesk Application Manager](https://applicationmanager.algodesk.io/) is a very useful tool to manage your polls. From the Algodesk dashboard you can delete your own polls or close-out polls you no longer want to participate in. As per polls' TEAL logic, if users close-out their participation in a poll before the vote is over, their votes will be nullified.

## Starting the web application

If you want to start locally your application you have to create you database schema in your PosgresSQL database. You can find it in databse_schema.sql
After this you have to start the java application through the main and start the frontend using the command *'npm run start'* from the relative path /src/main/frontend/
Now you can reach the application at the link http://localhost/9090

You can find the application deployed at this link [Block'n'Poll](https://blockandpoll.herokuapp.com/)

When you first open the application you have to wait some minutes in order to allow [Hekoru](https://www.heroku.com/) to start the application (being a free account the host turns off the deployed application when unused).

Being a demonstrative web application Block'n'Poll interacts only with Algorand Testnet, so you users need a Testnet account.