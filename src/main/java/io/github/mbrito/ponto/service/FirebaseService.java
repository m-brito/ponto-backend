package io.github.mbrito.ponto.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseService {

	@Value("${firebase.config.path}")
	private String firebaseConfigPath;
	
	@Value("${firebase.bucket}")
	private String firebaseBucket;
	
	@Value("${firebase.project.id}")
	private String firebaseProjectId;

	@Bean
	public FirebaseApp firebaseApp() throws IOException {

		FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

		FirebaseOptions options = FirebaseOptions.builder()
				.setProjectId(firebaseProjectId)
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
//				.setDatabaseUrl("https://ponto-springboot.firebaseio.com")
				.setStorageBucket(firebaseBucket).build();
		return FirebaseApp.initializeApp(options);
	}
	
	public String saveImage(byte[] imageData, String fileName) throws IOException {
		Bucket bucket = StorageClient.getInstance().bucket();
		Path tempFile = Files.createTempFile(Paths.get(System.getProperty("java.io.tmpdir")), fileName, "");
		Files.write(tempFile, imageData);
		Blob blob = bucket.create("perfilUsuario/"+fileName, Files.readAllBytes(tempFile));
		Acl acl = Acl.of(Acl.User.ofAllUsers(), Acl.Role.OWNER);
		blob.createAcl(acl);
		return blob.getMediaLink();
	}
	
	public void deleteImageByLink(String link) {
	    String encodedFileName = link.substring(link.lastIndexOf("/") + 1, link.indexOf("?"));
	    String fileName = java.net.URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8);
	    System.out.println(fileName);

	    Storage storage = StorageOptions.getDefaultInstance().getService();
	    BlobId blobId = BlobId.of(firebaseBucket, fileName);
	    Blob blob = storage.get(blobId);
	    if (blob != null) {
	        boolean deleted = storage.delete(blobId);
	        System.out.println(deleted);
	        if (deleted) {
	            System.out.println("Imagem removida com sucesso");
	        } else {
	            System.out.println("Falha ao remover a imagem");
	        }
	    } else {
	        System.out.println("O arquivo não existe no Firebase Storage");
	    }
	}
}