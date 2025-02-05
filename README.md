# Nosto Assignment

## Overview

This project is a currency conversion application that provides a REST API for retrieving currency exchange rates and performing currency conversions.

## How to Run Locally

1. **Set up the API key:**

   - Locate the `application.properties.template` file.
   - Fill in the API key for the exchange rate service.
   - Rename the file to `application.properties`.

2. **Start the application:**

```
docker-compose up --build

```

3. **Access the application:**

- Open your web browser and navigate to `http://localhost:3000`

## API Endpoints

### Get All Currencies

- **Endpoint:** `/currencies`
- **Method:** GET
- **Description:** Retrieves a list of all available currencies.
- **Response:** A list of currency codes.

### Convert Currency

- **Endpoint:** `/convert`
- **Method:** GET
- **Description:** Converts an amount from one currency to another.
- **Parameters:**
  -- `source`: Source currency code
  -- `target`: Target currency code
  -- `amount`: Amount to convert
- **Response:** Conversion details including the converted amount.

## Key Components

1. **CurrencyConversionController:** Handles HTTP requests for currency-related operations.
2. **CurrencyConversionService:** Manages the business logic for currency conversion and exchange rate retrieval.

## Caching

The application uses caching to improve performance and reduce API calls:

- Exchange rates are cached with the key 'EUR'.
- The list of all currencies is cached to reduce computation time.

## Development

This project uses Nx for development. Here are some useful commands:

- Run development server: `npx nx dev nosto_assignment`
- Build for production: `npx nx build nosto_assignment`
- Show project details: `npx nx show project nosto_assignment`

## Additional Information

For more details about the Nx workspace setup and its capabilities, refer to the [Nx documentation](https://nx.dev/nx-api/next).

## Troubleshooting

If you encounter any issues while running the application, please check the following:

- Ensure Docker is installed and running on your system.
- Verify that the API key is correctly set in the `application.properties` file.
- Check the application logs for any error messages.
