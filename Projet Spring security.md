# SonarQube: Analyse de la qualité du code 

## Sécurité 
### Make sure disabling Spring Security's CSRF protection is safe here.
 **Solution :** Ajout d'un commentaire pour signifier que le CSRF est désactivé volontairement
```java
 // CSRF désactivé volontairement : API stateless authentifiée par JWT (pas de cookies de session)
```

### Revoke and change this password, as it is compromised.
 **Solution :** Ajout de variable dans la classe DataInitailizer et création d'un application.properties local avec changement des mots de passe.

### Add a nested comment explaining why this method is empty, throw an UnsupportedOperationException or complete the implementation
**Solution :** ajout d'un commentaire au dessus du constructeur vide 
```java
// Constructeur vide requis par JPA/Hibernate
```

---

## Maintenabilité

### Rename this package name to match the regular expression
**Solution :** retirer les majuscules au début des noms de package

### Remove the parentheses around the "session" parameter 
**Solution :** retrait des parenthèses

###  A "NullPointerException" could be thrown; "userConnected" is nullable here.
**Solution :** ajout avant le return de 
```java
if(userConnected == null) {  
	throw new IllegalStateException("Utilisateur non authentifié correctement");  
}
```


### Either replace the "@Value" annotation with a standard field initialization, use "${propertyName}" to inject a property or use "#{expression}" to evaluate a SpEL expression. 
**Solution :** remplacer la valeur en dur par une injection de propriété dans application.properties

---

## Autres

### Remove redundant visibility modifiers from this test class and its methods.
**Solution :** retrait des public devant les void de SpringSecurityApplicationTests

### Autre warning dont je n'ai pas récupéré la consigne
**Solution :** Création de DTO supplémentaires sur Book et User pour résoudre une alerte 