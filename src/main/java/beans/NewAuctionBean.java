package beans;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.file.UploadedFile;

import model.General;
import model.Item;
import model.User;
import viewModel.ItemDB;

@ManagedBean(name="newAuctionBean")
@ViewScoped
public class NewAuctionBean {
	private Item item;
	private String startPrice;
	private UploadedFile image;
	private boolean waiver;
	private String waiverValue;
	
	public NewAuctionBean() {
		item = new Item();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		item.setUser((User)session.getAttribute("user"));
		item.setStartDate(new Date());
		waiver = true;
	}

	public Item getItem() {
		return item;
	}

	public String getStartPrice() {
		return startPrice;
	}
	
	public boolean getWaiver() {
		return waiver;
	}
	
	public String getWaiverValue() {
		return waiverValue;
	}
	
	public UploadedFile getImage() {
		return image;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public void setWaiver(boolean waiver) {
		this.waiver = waiver;
	}

	public void setStartPrice(String startPrice) {
		this.startPrice = startPrice;
	}
	
	public void setWaiverValue(String waiverValue) {
		this.waiverValue = waiverValue;
	}
	
	public void setImage(UploadedFile image) {
		this.image = image;
	}

	public Date today() {
		return new Date();
	}
	
	public String confirmNewItem() {
		//upload image from file upload
		if(!upload())
			return "newAuction.xhtml";
		//upload to DB
		item.setWaiverPrice(-1);
		if(waiverValue != null)
			item.setWaiverPrice(Integer.parseInt(startPrice));
		item.setPictureURL(image.getFileName());
		item.setStartPrice(Integer.parseInt(startPrice));
		ItemDB itemDB = new ItemDB();
		itemDB.INSERT(item);
		
		//redirect to ongoing auctions
		return "ongoingAuctions.xhtml";
	}
	
	public boolean upload() {
		if (image != null) {
            try {
                String path = General.getProjectFolderPath() + "/" + General.getResourcesImagesPath();
                File targetFolder = new File(path);
                InputStream inputStream = image.getInputStream();
                String filename = image.getFileName(); // You might want to change this for uniqueness
                FileOutputStream out = new FileOutputStream(new File(targetFolder, filename));

                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                inputStream.close();
                out.flush();
                out.close();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Upload successful!"));
                return true;
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Upload failed! Try renaming the image!"));
                e.printStackTrace();
                return false;
            }
        }
		return false;
	}
}
