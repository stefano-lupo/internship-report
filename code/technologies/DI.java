public class PizzaOrderingService {
  private DatabaseLogger databaseLogger;
  private CustomerBiller customerBiller;

  @Inject
  public PizzaOrderingService(DatabaseLogger databaseLogger, CustomerBiller customerBiller) {
    this.databaseLogger = databaseLogger
    this.customerBiller = customerBiller
  }
}