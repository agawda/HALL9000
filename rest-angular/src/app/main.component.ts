import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { BookService } from './book.service';
import { Book } from './book';

@Component({
  selector: 'app-books',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})

export class MainComponent {
  title = 'books';

  private TITLE = "title";
  private AUTHOR = "authors";
  private CATEGORY = "categories";
  private PRICE = "retailPriceAmount";
  private PROMO = "discount";
  private ASC = "ascending";
  private DSC = "descending";
  private NONE = "none";
  private BOOKS_PER_PAGE = 20;

  books: Book[];
  Math: any;
  sortBy: string; //title, author, price, promo
  orderBy: string; //ascending, descending, null

  currentPage: number;
  maxPages: number;

  constructor(private bookService: BookService, private router: Router) {
    this.Math = Math;

    this.orderBy = this.NONE;
    this.sortBy = this.NONE;
    this.currentPage = 0;

    bookService.getTotalBooks().then(maxBooks => {
      this.maxPages = Math.floor(maxBooks / this.BOOKS_PER_PAGE);
    });
    bookService.getPage(this.currentPage).then(books => this.books = books);
  }

  nextPage() {
    if(this.currentPage === this.maxPages) return;
    this.currentPage++;
    if(this.sortBy !== this.NONE) this.sortRequest();
    else this.getRequest();
  }

  previousPage() {
    if(this.currentPage === 0) return;
    this.currentPage--;
    if(this.sortBy !== this.NONE) this.sortRequest();
    else this.getRequest();
  }

  firstPage() {
    this.currentPage = 0;
    if(this.sortBy !== this.NONE) this.sortRequest();
    else this.getRequest();
  }

  lastPage() {
    this.currentPage = this.maxPages;
    if(this.sortBy !== this.NONE) this.sortRequest();
    else this.getRequest();
  }

  searchBooks(param: string) {
    this.bookService.searchBooks(param).subscribe(books => {
      this.books = books;
    });
  }

  sortByTitle() {
    this.checkIfSorted(this.TITLE);
    this.sortBy = this.TITLE;
    this.orderRequest();
    this.sortRequest();
  }

  sortByAuthor() {
    this.checkIfSorted(this.AUTHOR);
    this.sortBy = this.AUTHOR;
    this.orderRequest();
    this.sortRequest();
  }

  sortByCategory() {
    this.checkIfSorted(this.CATEGORY);
    this.sortBy = this.CATEGORY;
    this.orderRequest();
    this.sortRequest();
  }

  sortByPrice() {
    this.checkIfSorted(this.PRICE);
    this.sortBy = this.PRICE;
    this.orderRequest();
    this.sortRequest();
  }

  sortByPromo() {
    this.checkIfSorted(this.PROMO);
    this.sortBy = this.PROMO;
    this.orderRequest();
    this.sortRequest();
  }

  private orderRequest() {
    if(this.orderBy === this.NONE || this.orderBy === this.DSC) {
      this.orderBy = this.ASC;
    } else if(this.orderBy === this.ASC) {
      this.orderBy = this.DSC;
    }
  }

  private sortRequest() {
    this.bookService.getBooksSorted(this.sortBy, this.orderBy, this.currentPage).subscribe(books => this.books = books);
  }

  private getRequest() {
    this.bookService.getPage(this.currentPage).then(books => this.books = books);
  }

  private checkIfSorted(param: string) {
    if(this.sortBy !== param && this.orderBy !== this.NONE) this.orderBy = this.NONE;
  }

  reset() {
    this.getRequest();
    this.sortBy = this.NONE;
    this.orderBy = this.NONE;
  }

  filter(title: string, author: string, category: string, bookstore: string, min: number, max: number) {
    this.bookService.advancedSearchBooks(title, author, category, bookstore, min, max)
      .then(books=>this.books = books);
  }
}
