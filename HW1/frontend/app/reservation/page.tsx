"use client";

import { useQuery } from "@tanstack/react-query";
import { Trip } from "@/types/trip";
import { getTrip } from "@/service/tripService";
import TripCard from "@/components/TripCard";
import { useCookies } from "next-client-cookies";
import { Button, Chip, Input, Skeleton, Spinner } from "@nextui-org/react";
import { useState } from "react";
import { User } from "@/types/user";
import { createReservation } from "@/service/reservationService";
import { useRouter } from "next/navigation";
import { Currency } from "@/types/currency";

export default function Trips() {
  const cookies = useCookies();
  const router = useRouter();
  const tripId = parseInt(cookies.get("trip") || "0");
  const currency: Currency = (cookies.get("currency") as Currency) || "EUR";
  const isLoggedIn = cookies.get("user") !== undefined;
  const user = isLoggedIn ? (JSON.parse(cookies.get("user")!) as User) : null;

  const { isPending: isTripPending, data: trip } = useQuery<Trip>({
    queryKey: ["trip", tripId, currency],
    queryFn: () => getTrip(tripId, { currency }),
  });

  const [seats, setSeats] = useState<number>(
    parseInt(cookies.get("seats") || "1")
  );

  const submit = async () => {
    const reservation = await createReservation({
      user: {
        id: user?.id || 0,
      },
      trip: {
        id: tripId,
      },
      seats,
    });

    cookies.set("reservation", reservation.id.toString());
    router.push("/reservation/success");
  };

  return (
    <div className="flex flex-col gap-8">
      <div className="p-8 lg:p-24 flex flex-col items-center justify-center gap-8">
        <h1 className="font-bold text-4xl lg:text-6xl text-center text-balance">
          Book your trip.
        </h1>
      </div>
      <div className="flex flex-col px-4 md:px-8 lg:px-16 gap-8 self-center w-[512px]">
        {!isLoggedIn && (
          <Chip color="danger" id="signInChip" className="self-center">
            Sign in first, then refresh the page.
          </Chip>
        )}
        <TripCard trip={trip!} isLoaded={!isTripPending} clickable={false} />
        <Skeleton isLoaded={!isTripPending} className="rounded-lg">
          <Input
            label="Seats"
            type="number"
            id="seatsInput"
            defaultValue={cookies.get("seats") || "1"}
            min={1}
            max={trip ? trip.freeSeats : 100}
            onValueChange={(value: string) => setSeats(parseInt(value))}
          />
        </Skeleton>
        <div className="flex flex-row justify-between items-center">
          <Skeleton isLoaded={!isTripPending} className="rounded-lg">
            <p>
              <span className="font-semibold">Total</span>:{" "}
              {trip
                ? (seats * trip?.price).toLocaleString("pt-PT", {
                    style: "currency",
                    currency: cookies.get("currency") || "EUR",
                  })
                : "0 EUR"}
            </p>
          </Skeleton>
          <Button color="primary" id="buyBtn" onClick={submit} isDisabled={!isLoggedIn}>
            Buy
          </Button>
        </div>
      </div>
    </div>
  );
}
