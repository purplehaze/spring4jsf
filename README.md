# spring4jsf
An bootstrap/starter dependency to start JSF on spring boot project
JSF, Primefaces (inclusive upload file funktionality), omnifaces are preconfigured

All you need in your project is to add maven dependency to you projects pom:
```
		<dependency>
			<groupId>net.smart4life</groupId>
			<artifactId>spring4jsf</artifactId>
			<version>1.0.0-SNAPSHOT</version>	
		</dependency>
```
### XHTML content
Place your .xhtml pages to ${project_location}/src/main/resources/META-INF/resources directory.

### JAVA view controller
And annotate your first (and hopefully next) view controllers:
```
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.smart4life.spring4jsf.scope.viewaccess.ViewAccessScoped;

@Getter
@Slf4j
@Component
@ViewAccessScoped
public class BankAccountsController {
...
}
```
Following JSF scopes are supported (please see net.smart4life.spring4jsf.scope package in project)
- ApplicationScope (native spring supported)
- RequestScope (native spring supported)
- SessionScope (native spring supported)
- FlashScope (custom implementation in project)
- ViewScope (custom implementation in project)
- ViewAccessScope (custom implementation in project)

### More configurable JSF/Spring environment
If you need more configuration possibilities or don't want use primefaces as JSF library you can visit [joinfaces.org](http://joinfaces.org/) page.
This project gives you much more flexibility (and some additional project dependencies).


