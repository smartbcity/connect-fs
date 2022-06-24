

## Commands





  
<article>

**fileDelete** ( cmd: [`FileDeleteCommand`](#command) ) : [`FileDeletedEvent`](#event) <br/> *Access: fs_write_file* 

Delete a file

</article>
<article>

**fileUpload** ( cmd: [`FileUploadCommand`](#command) , file: `FilePart` ) <br/> *Access: fs_write_file* 

Upload a file

</article>
<article>

**fileUploads** ( commands: [`HashMap<String, FileUploadCommand>`](#command) , files: `Flux<FilePart>` ) <br/> *Access: fs_write_file* 

Upload multiple files

</article>
<article>

**initPublicDirectory** ( cmd: [`FileInitPublicDirectoryCommand`](#command) ) : [`FilePublicDirectoryInitializedEvent`](#event) <br/> *Access: fs_write_policy* 

Grant public access to a given directory

</article>
<article>

**revokePublicDirectory** ( cmd: [`FileRevokePublicDirectoryCommand`](#command) ) : [`FilePublicDirectoryRevokedEvent`](#event) <br/> *Access: fs_write_policy* 

Revoke public access to a given directory

</article>

